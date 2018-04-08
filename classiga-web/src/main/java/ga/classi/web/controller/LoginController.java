package ga.classi.web.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ga.classi.commons.helper.ActionResult;
import ga.classi.commons.helper.CommonConstants;
import ga.classi.commons.helper.DefaultUser;
import ga.classi.commons.helper.PasswordUtils;
import ga.classi.commons.helper.StringConstants;
import ga.classi.commons.web.helper.JSON;
import ga.classi.web.controller.base.BaseControllerAdapter;
import ga.classi.web.helper.ModelKeyConstants;
import ga.classi.web.helper.SessionKeyConstants;
import ga.classi.web.helper.SessionManager;
import ga.classi.web.helper.URLParameterKeyContants;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author eatonmunoz
 */
@Slf4j
@Controller
public class LoginController extends BaseControllerAdapter {

    @SuppressWarnings("unchecked")
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

            JSONObject parameters = new JSONObject();
            parameters.put("username", username);
            parameters.put("password", password);
            
            ActionResult result = login(parameters);

            JSONObject user;
            
            if (result != null) {
                
                String status = result.getStatus();
                
                if (CommonConstants.SUCCESS.equals(status)) {
                    user = (JSONObject) result.getContent();
                } else {
                    // Check default user
                    user = checkDefaultUser(username, password);
                    log.debug("User: {}", user);
                }

                if (user == null) {
                    return viewAndNotifyError("login", model, result.getMessage());
                }
                
                JSONObject userGroup = (JSONObject) user.get("userGroup");
                JSONArray menuPermissions = (JSONArray) userGroup.get("menuPermissions");
                
                SessionManager.set(SessionKeyConstants.USER, JSON.remove(user, "userGroup"));
                SessionManager.set(SessionKeyConstants.USER_GROUP, JSON.remove(userGroup, "menuPermissions"));
                SessionManager.set(SessionKeyConstants.MENU_PERMISSIONS, menuPermissions);

                // Trigger menu loading
                loadAllowedMenus();
                
                String redirect = httpServletRequest.getParameter(URLParameterKeyContants.REDIRECT);

                if (redirect != null && !redirect.isEmpty()) {
                    return redirectAndNotifySuccess(URLDecoder.decode(redirect, "UTF-8"), messageHelper.getMessage("success.loginsuccessfully"));
                }
                
                return redirectAndNotifySuccess(getDefaultRedirect(), messageHelper.getMessage("success.loginsuccessfully"));
            }
        }

        return view("login", model);
    }

    @SuppressWarnings("unchecked")
    private JSONObject checkDefaultUser(String username, String password) {
        log.debug("Check default user ...");
        String defaultUser = usersProperties.getProperty(username);
        if ((defaultUser == null) || defaultUser.trim().isEmpty()) {
            return null;
        }
        String defaultUserFullName = StringConstants.EMPTY;
        String[] dataUserSplit = defaultUser.split(",");
        switch (dataUserSplit.length) {
            case 1:    // Only contains password
                defaultUserFullName = DefaultUser.chooseName();
                break;
            case 2:    // Only contains password and first name
                defaultUserFullName = dataUserSplit[1].trim();
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
            user.put("fullName", defaultUserFullName);
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
