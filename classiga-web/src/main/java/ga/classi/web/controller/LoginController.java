package ga.classi.web.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ga.classi.commons.helper.CommonConstants;
import ga.classi.commons.helper.DefaultUser;
import ga.classi.commons.helper.HttpClient;
import ga.classi.commons.helper.HttpClientResponse;
import ga.classi.commons.helper.PasswordUtils;
import ga.classi.commons.helper.StringConstants;
import ga.classi.web.helper.JSONHelper;
import ga.classi.web.helper.ModelKeyConstants;
import ga.classi.web.helper.SessionKeyConstants;
import ga.classi.web.helper.SessionManager;
import ga.classi.web.helper.URLParameterKeyContants;

/**
 *
 * @author eatonmunoz
 */
@Controller
public class LoginController extends HttpClientBaseController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = {"/", "/login"}, method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView login(
            @RequestParam(name = "username", required = false, defaultValue = StringConstants.EMPTY) String username,
            @RequestParam(name = "password", required = false, defaultValue = StringConstants.EMPTY) String password) throws IOException {

        log.debug("Login form ...");

        Map<String, Object> model = new HashMap<String, Object>();
        model.put(ModelKeyConstants.USERNAME, username);
        model.put(ModelKeyConstants.PASSWORD, password);
        
        if (isPost()) {

            log.debug("Process login ...");

            HttpClient httpClient = getDefinedHttpClient("/login");
            httpClient.addParameter("username", username);
            httpClient.addParameter("password", password);
            
            HttpClientResponse response = httpClient.post();

            JSONObject user;
            
            if (response != null) {
                
                String status = response.getStatus();
                
                if (CommonConstants.SUCCESS.equals(status)) {
                    user = (JSONObject) response.getContent();
                } else {
                    // Check default user
                    user = checkDefaultUser(username, password);
                    log.debug("User: {}", user);
                }

                if (user == null) {
                    return viewAndNotifyError("login", model, response.getMessage());
                }
                
                JSONObject userGroup = (JSONObject) user.get("userGroup");
                JSONArray menuPermissions = (JSONArray) userGroup.get("menuPermissions");
                
                SessionManager.set(SessionKeyConstants.USER, JSONHelper.remove(user, "userGroup"));
                SessionManager.set(SessionKeyConstants.USER_GROUP, JSONHelper.remove(userGroup, "menuPermissions"));
                SessionManager.set(SessionKeyConstants.MENU_PERMISSIONS, menuPermissions);

                // Trigger menu loading
                loadAllowedMenus();
                
                String redirect = httpServletRequest.getParameter(URLParameterKeyContants.REDIRECT);

                if (redirect != null && !redirect.isEmpty()) {
                    return redirectAndNotifySuccess(URLDecoder.decode(redirect, "UTF-8"), messageHelper.getMessage("success.loginsuccessfully"));
                }
                
                return redirectAndNotifySuccess(defaultRedirect, messageHelper.getMessage("success.loginsuccessfully"));
            }
        }

        return view("login", model);
    }

    @SuppressWarnings("unchecked")
    private JSONObject checkDefaultUser(String username, String password) {
        log.debug("Check default user ...");
        String defaultUser = usersProp.getProperty(username);
        if ((defaultUser == null) || defaultUser.trim().isEmpty()) {
            return null;
        }
        String defaultUserFirstName = StringConstants.EMPTY;
        String defaultUserLastName = StringConstants.EMPTY;
        String[] dataUserSplit = defaultUser.split(",");
        switch (dataUserSplit.length) {
            case 1:    // Only contains password
                String[] name = DefaultUser.chooseName();
                defaultUserFirstName = name[0];
                defaultUserLastName = name[1];
                break;
            case 2:    // Only contains password and first name
                defaultUserFirstName = dataUserSplit[1].trim();
                break;
            case 3:    // Contains password, first name, and last name
                defaultUserFirstName = dataUserSplit[1].trim();
                defaultUserLastName = dataUserSplit[2].trim();
                break;
            default:

        }
        String defaultUserPassword = dataUserSplit[0].trim();
        String stirredPassword = PasswordUtils.stir(password);
        if ((defaultUserPassword != null) && defaultUserPassword.equals(stirredPassword)) {
            JSONObject userGroup = new JSONObject();
            userGroup.put("id", DefaultUser.USER_GROUP_ID);
            userGroup.put("name", DefaultUser.USER_GROUP_NAME);
            userGroup.put("menuPermissions", new JSONArray());
            JSONObject user = new JSONObject();
            user.put("id", DefaultUser.USER_ID);
            user.put("username", username);
            user.put("firstName", defaultUserFirstName);
            user.put("lastName", defaultUserLastName);
            user.put("userGroup", userGroup);
            return user;
        }
        return null;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout() {
        if (SessionManager.get(SessionKeyConstants.USER) != null) {
            SessionManager.destroy();
            return redirectAndNotifySuccess("login", messageHelper.getMessage("success.logoutsuccessfully"));
        }
        return redirect("login");
    }
    
}
