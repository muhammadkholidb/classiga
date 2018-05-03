package ga.classi.web.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ga.classi.commons.helper.ActionResult;
import ga.classi.commons.helper.CommonConstants;
import ga.classi.commons.helper.CommonUtils;
import ga.classi.commons.helper.StringConstants;
import ga.classi.commons.web.helper.JSON;
import ga.classi.web.controller.base.BaseControllerAdapter;
import ga.classi.web.helper.ModelKeyConstants;
import ga.classi.web.helper.UIHelper;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author eatonmunoz
 */
@Slf4j
@Controller
public class UserController extends BaseControllerAdapter {

    @GetMapping(value = "/settings/user")
    public ModelAndView index() {
        log.info("Index ...");
        return view("user/list");
    }

    private JSONArray getUserGroups() {
        log.info("Get user groups ..."); 
        ActionResult result = listUserGroup(null);
        if (result != null && CommonConstants.SUCCESS.equals(result.getStatus())) {
            return (JSONArray) result.getContent();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/settings/user/add", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView add(
            @RequestParam(name = "fullName", required = true, defaultValue = StringConstants.EMPTY) String fullName,
            @RequestParam(name = "username", required = true, defaultValue = StringConstants.EMPTY) String username,
            @RequestParam(name = "email", required = true, defaultValue = StringConstants.EMPTY) String email,
            @RequestParam(name = "password", required = true, defaultValue = StringConstants.EMPTY) String password,
            @RequestParam(name = "userGroupId", required = true, defaultValue = StringConstants.EMPTY) String userGroupId) {

        Map<String, Object> model = new HashMap<String, Object>();
        model.put(ModelKeyConstants.FULL_NAME, fullName);
        model.put(ModelKeyConstants.USERNAME, username);
        model.put(ModelKeyConstants.EMAIL, email);
        model.put(ModelKeyConstants.PASSWORD, password);
        model.put(ModelKeyConstants.USER_GROUP_ID, userGroupId);

        JSONArray userGroups = getUserGroups();
        model.put(ModelKeyConstants.USER_GROUPS, userGroups);

        if (isPost()) {

            // Check if submitted username and email equal to default user's username
            for (Object key : usersProperties.keySet()) {
                if (username.equalsIgnoreCase(key.toString()) || email.equalsIgnoreCase(key.toString())) {
                    return viewAndNotifyError("user/form-add", model, messageHelper.getMessage("error.usernamenotallowed"));
                }
            }
            
            JSONObject jsonUser = new JSONObject();
            jsonUser.put("fullName", fullName.trim());
            jsonUser.put("username", username.trim());
            jsonUser.put("email", email.trim());
            jsonUser.put("password", password.trim());
            jsonUser.put("active", CommonConstants.YES);
            jsonUser.put("userGroupId", userGroupId);

            ActionResult result = addUser(jsonUser);
            
            if (CommonConstants.SUCCESS.equals(result.getStatus())) {
                return redirectAndNotifySuccess("/settings/user", result.getMessage());
            } else {    // Fail or error
                return viewAndNotifyError("user/form-add", model, result.getMessage());
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
            @RequestParam(name = "fullName", required = true, defaultValue = StringConstants.EMPTY) String fullName,
            @RequestParam(name = "username", required = true, defaultValue = StringConstants.EMPTY) String username,
            @RequestParam(name = "email", required = true, defaultValue = StringConstants.EMPTY) String email,
            @RequestParam(name = "password", required = true, defaultValue = StringConstants.EMPTY) String password,
            @RequestParam(name = "userGroupId", required = true, defaultValue = StringConstants.EMPTY) String userGroupId,
            @RequestParam(name = "active", required = true, defaultValue = CommonConstants.NO) String active) {

        if (userId == null || userId.isEmpty()) {
            return redirectAndNotifyError("/settings/user", messageHelper.getMessage("error.usernotfound"));
        }

        JSONObject user;

        JSONObject paramsFind = new JSONObject();
        paramsFind.put("id", userId);

        ActionResult resultFind = findUser(paramsFind);
        
        if (CommonConstants.SUCCESS.equals(resultFind.getStatus())) {
            user = (JSONObject) resultFind.getContent();
        } else {    // Fail or error
            return redirectAndNotifyError("/settings/user", resultFind.getMessage());
        }

        JSONArray userGroups = getUserGroups();

        if (isPost()) {

            JSONObject jsonUser = new JSONObject();
            jsonUser.put("fullName", fullName.trim());
            jsonUser.put("username", username.trim());
            jsonUser.put("email", email.trim());
            jsonUser.put("password", password.trim());
            jsonUser.put("active", active.trim());
            jsonUser.put("userGroupId", userGroupId);
            jsonUser.put("id", userId);

            ActionResult result = editUser(jsonUser);
            
            if (CommonConstants.SUCCESS.equals(result.getStatus())) {

                return redirectAndNotifySuccess("/settings/user", result.getMessage());

            } else {    // Fail or error

                Map<String, Object> model = new HashMap<String, Object>();
                model.put(ModelKeyConstants.FULL_NAME, fullName);
                model.put(ModelKeyConstants.USERNAME, username);
                model.put(ModelKeyConstants.EMAIL, email);
                model.put(ModelKeyConstants.PASSWORD, password);
                model.put(ModelKeyConstants.USER_GROUP_ID, userGroupId);
                model.put(ModelKeyConstants.ACTIVE, active);
                model.put(ModelKeyConstants.USER_ID, userId);
                model.put(ModelKeyConstants.USER_GROUPS, userGroups);

                return viewAndNotifyError("user/form-edit", model, result.getMessage());
            }
        }

        JSONObject userGroup = (JSONObject) user.get("userGroup");
        
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(ModelKeyConstants.FULL_NAME, user.get("fullName"));
        model.put(ModelKeyConstants.USERNAME, user.get("username"));
        model.put(ModelKeyConstants.EMAIL, user.get("email"));
        model.put(ModelKeyConstants.PASSWORD, password);
        model.put(ModelKeyConstants.USER_GROUP_ID, userGroup.get("id"));
        model.put(ModelKeyConstants.ACTIVE, user.get("active"));
        model.put(ModelKeyConstants.USER_ID, user.get("id"));
        model.put(ModelKeyConstants.USER_GROUPS, userGroups);

        if (userGroups == null || userGroups.isEmpty()) {
            return viewAndNotifyWarning("user/form-edit", model, messageHelper.getMessage("warn.emptyusergroups"));
        }

        return view("user/form-edit", model);
    }

    @SuppressWarnings("unchecked")
    @PostMapping(value = "/settings/user/remove")
    public ModelAndView remove(@RequestParam(name = "selected", required = false) String[] selected) {

        if (selected == null || selected.length == 0) {
            return redirect("/settings/user");
        }
        
        JSONObject params = new JSONObject();
        params.put("id", Arrays.toString(selected));

        ActionResult result = removeUser(params);
        
        if (CommonConstants.SUCCESS.equals(result.getStatus())) {

            return redirectAndNotifySuccess("/settings/user", result.getMessage());

        } else {    // Fail or error

            return redirectAndNotifyError("/settings/user", result.getMessage());
        }
    }

    @GetMapping("/user/change-password")
    public ModelAndView changePassword(ModelMap modelMap) {
        log.info("Change password page ...");
        log.debug("Model: \n{}", JSON.stringify(modelMap, true));
        return view("user/change-password", modelMap);
    }
    
    @PostMapping(value = "/user/change-password")
    public ModelAndView changePassword(
            @RequestParam(name = "oldPassword", required = false) String oldPassword,
            @RequestParam(name = "newPassword", required = false) String newPassword,
            @RequestParam(name = "newPasswordConfirm", required = false) String newPasswordConfirm, RedirectAttributes ra) {

        log.info("Submit change password ...");
        
        log.debug("Logged in user: \n{}", JSON.stringify(getLoggedInUser(), true)); 
        
        ActionResult result = changePassword(CommonUtils.map("oldPassword", oldPassword, "newPassword", newPassword, "newPasswordConfirm", newPassword, "id", getLoggedInUser().get("id")));
        
        if (result.isSuccess()) {
            ra.addFlashAttribute(ModelKeyConstants.NOTIFY, UIHelper.createSuccessNotification(result.getMessage()));
            return redirect("/user/change-password", ra);
        }
        
        ra.addFlashAttribute("oldPassword", oldPassword);
        ra.addFlashAttribute("newPassword", newPassword);
        ra.addFlashAttribute("newPasswordConfirm", newPasswordConfirm);
        ra.addFlashAttribute(ModelKeyConstants.NOTIFY, UIHelper.createErrorNotification(result.getMessage()));
        
        return redirect("/user/change-password", ra);

    }

}
