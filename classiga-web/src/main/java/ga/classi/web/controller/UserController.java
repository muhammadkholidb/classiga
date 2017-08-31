package ga.classi.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ga.classi.commons.helper.CommonConstants;
import ga.classi.commons.helper.HttpClient;
import ga.classi.commons.helper.HttpClientResponse;
import ga.classi.commons.helper.StringConstants;
import ga.classi.web.helper.ModelKeyConstants;
import java.util.Arrays;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author eatonmunoz
 */
@Controller
public class UserController extends HttpClientBaseController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping(value = "/settings/user")
    public ModelAndView index() throws IOException {
        log.info("Index ...");
        return view("user/list");
    }

    private JSONArray getUserGroups() throws IOException {
        log.info("Get user groups ...");
        HttpClientResponse response = getDefinedHttpClient("/settings/user-group/list").get();
        if (response != null) {
            if (CommonConstants.SUCCESS.equals(response.getStatus())) {
                return (JSONArray) response.getContent();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/settings/user/add", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView add(
            @RequestParam(name = "firstName", required = true, defaultValue = StringConstants.EMPTY) String firstName,
            @RequestParam(name = "lastName", required = true, defaultValue = StringConstants.EMPTY) String lastName,
            @RequestParam(name = "username", required = true, defaultValue = StringConstants.EMPTY) String username,
            @RequestParam(name = "email", required = true, defaultValue = StringConstants.EMPTY) String email,
            @RequestParam(name = "password", required = true, defaultValue = StringConstants.EMPTY) String password,
            @RequestParam(name = "userGroupId", required = true, defaultValue = StringConstants.EMPTY) String userGroupId) throws IOException {

        Map<String, Object> model = new HashMap<String, Object>();
        model.put(ModelKeyConstants.FIRST_NAME, firstName);
        model.put(ModelKeyConstants.LAST_NAME, lastName);
        model.put(ModelKeyConstants.USERNAME, username);
        model.put(ModelKeyConstants.EMAIL, email);
        model.put(ModelKeyConstants.PASSWORD, password);
        model.put(ModelKeyConstants.USER_GROUP_ID, userGroupId);

        JSONArray userGroups = getUserGroups();
        model.put(ModelKeyConstants.USER_GROUPS, userGroups);

        if (isPost()) {

            JSONObject jsonUser = new JSONObject();
            jsonUser.put("firstName", firstName.trim());
            jsonUser.put("lastName", lastName.trim());
            jsonUser.put("username", username.trim());
            jsonUser.put("email", email.trim());
            jsonUser.put("password", password.trim());
            jsonUser.put("active", CommonConstants.YES);
            jsonUser.put("userGroupId", userGroupId);

            HttpClientResponse response = getDefinedHttpClient("/settings/user/add", jsonUser).post();

            if (CommonConstants.SUCCESS.equals(response.getStatus())) {
                return redirectAndNotifySuccess("/settings/user", response.getMessage());
            } else {    // Fail or error
                return viewAndNotifyError("user/form-add", model, response.getMessage());
            }
        }

        if (userGroups == null || userGroups.isEmpty()) {
            return viewAndNotifyWarning("user/form-add", model, messageHelper.getMessage("warn.emptyusergroups"));
        }

        return view("user/form-add", model);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/settings/user/edit/{userId}", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView edit(
            @PathVariable String userId,
            @RequestParam(name = "firstName", required = true, defaultValue = StringConstants.EMPTY) String firstName,
            @RequestParam(name = "lastName", required = true, defaultValue = StringConstants.EMPTY) String lastName,
            @RequestParam(name = "username", required = true, defaultValue = StringConstants.EMPTY) String username,
            @RequestParam(name = "email", required = true, defaultValue = StringConstants.EMPTY) String email,
            @RequestParam(name = "password", required = true, defaultValue = StringConstants.EMPTY) String password,
            @RequestParam(name = "userGroupId", required = true, defaultValue = StringConstants.EMPTY) String userGroupId,
            @RequestParam(name = "active", required = true, defaultValue = StringConstants.EMPTY) String active) throws IOException {

        if (userId == null || userId.isEmpty()) {

        }

        JSONObject user;

        JSONObject paramsFind = new JSONObject();
        paramsFind.put("id", userId);

        HttpClientResponse responseFind = getDefinedHttpClient("/settings/user/find", paramsFind).get();
        if (CommonConstants.SUCCESS.equals(responseFind.getStatus())) {
            user = (JSONObject) responseFind.getContent();
        } else {    // Fail or error
            return redirectAndNotifyError("/settings/user", responseFind.getMessage());
        }

        JSONArray userGroups = getUserGroups();

        if (isPost()) {

            JSONObject jsonUser = new JSONObject();
            jsonUser.put("firstName", firstName.trim());
            jsonUser.put("lastName", lastName.trim());
            jsonUser.put("username", username.trim());
            jsonUser.put("email", email.trim());
            jsonUser.put("password", password.trim());
            jsonUser.put("active", active.trim());
            jsonUser.put("userGroupId", userGroupId);
            jsonUser.put("id", userId);

            HttpClientResponse response = getDefinedHttpClient("/settings/user/edit", jsonUser).post();

            if (CommonConstants.SUCCESS.equals(response.getStatus())) {

                return redirectAndNotifySuccess("/settings/user", response.getMessage());

            } else {    // Fail or error

                Map<String, Object> model = new HashMap<String, Object>();
                model.put(ModelKeyConstants.FIRST_NAME, firstName);
                model.put(ModelKeyConstants.LAST_NAME, lastName);
                model.put(ModelKeyConstants.USERNAME, username);
                model.put(ModelKeyConstants.EMAIL, email);
                model.put(ModelKeyConstants.PASSWORD, password);
                model.put(ModelKeyConstants.USER_GROUP_ID, userGroupId);
                model.put(ModelKeyConstants.ACTIVE, active);
                model.put(ModelKeyConstants.USER_ID, userId);
                model.put(ModelKeyConstants.USER_GROUPS, userGroups);

                return viewAndNotifyError("user/form-edit", model, response.getMessage());
            }
        }

        Map<String, Object> model = new HashMap<String, Object>();
        model.put(ModelKeyConstants.FIRST_NAME, user.get("firstName"));
        model.put(ModelKeyConstants.LAST_NAME, user.get("lastName"));
        model.put(ModelKeyConstants.USERNAME, user.get("username"));
        model.put(ModelKeyConstants.EMAIL, user.get("email"));
        model.put(ModelKeyConstants.PASSWORD, password);
        model.put(ModelKeyConstants.USER_GROUP_ID, user.get("userGroupId"));
        model.put(ModelKeyConstants.ACTIVE, user.get("active"));
        model.put(ModelKeyConstants.USER_ID, user.get("id"));
        model.put(ModelKeyConstants.USER_GROUPS, userGroups);

        if (userGroups == null || userGroups.isEmpty()) {
            return viewAndNotifyWarning("user/form-edit", model, messageHelper.getMessage("warn.emptyusergroups"));
        }

        return view("user/form-edit", model);
    }

    @PostMapping(value = "/settings/user/remove")
    public ModelAndView remove(@RequestParam(name = "selected", required = false) String[] selected) throws IOException {

        if (selected == null || selected.length == 0) {
            return redirect("/settings/user");
        }
        
        HttpClient httpClient = getDefinedHttpClient("/settings/user/remove");

        httpClient.addParameter("id", Arrays.toString(selected));

        HttpClientResponse response = httpClient.post();

        if (CommonConstants.SUCCESS.equals(response.getStatus())) {

            return redirectAndNotifySuccess("/settings/user", response.getMessage());

        } else {    // Fail or error

            return redirectAndNotifyError("/settings/user", response.getMessage());
        }
    }

}
