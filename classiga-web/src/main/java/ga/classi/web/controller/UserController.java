package ga.classi.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ga.classi.commons.helper.ActionResult;
import ga.classi.commons.helper.CommonConstants;
import ga.classi.commons.helper.CommonUtils;
import ga.classi.commons.helper.DefaultUser;
import ga.classi.commons.helper.StringConstants;
import ga.classi.commons.web.helper.MultipartFileUtils;
import ga.classi.web.controller.base.BaseControllerAdapter;
import ga.classi.web.helper.ModelKeyConstants;
import ga.classi.web.helper.SessionKeyConstants;
import ga.classi.web.helper.SessionManager;
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

        Map<String, Object> model = new HashMap<>();
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

            Map<String, Object> model = new HashMap<>();
            model.put(ModelKeyConstants.FULL_NAME, fullName);
            model.put(ModelKeyConstants.USERNAME, username);
            model.put(ModelKeyConstants.EMAIL, email);
            model.put(ModelKeyConstants.PASSWORD, password);
            model.put(ModelKeyConstants.USER_GROUP_ID, userGroupId);
            model.put(ModelKeyConstants.ACTIVE, active);
            model.put(ModelKeyConstants.USER_ID, userId);
            model.put(ModelKeyConstants.USER_GROUPS, userGroups);

            // Check if submitted username and email equal to default user's username
            for (Object key : usersProperties.keySet()) {
                if (username.equalsIgnoreCase(key.toString()) || email.equalsIgnoreCase(key.toString())) {
                    return viewAndNotifyError("user/form-edit", model, messageHelper.getMessage("error.usernamenotallowed"));
                }
            }

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

                return viewAndNotifyError("user/form-edit", model, result.getMessage());
            }
        }

        JSONObject userGroup = (JSONObject) user.get("userGroup");
        
        Map<String, Object> model = new HashMap<>();
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

        JSONObject user = getLoggedInUser();

        if ((user != null) && DefaultUser.USER_ID.equals(user.get("id"))) {
            log.warn("Change password is not supported for default user, redirect ...");
            return redirect(getDefaultRedirect());
        }
        
        return view("user/change-password", modelMap);
    }
    
    @PostMapping(value = "/user/change-password")
    public ModelAndView changePassword(
            @RequestParam(name = "oldPassword", required = false) String oldPassword,
            @RequestParam(name = "newPassword", required = false) String newPassword,
            @RequestParam(name = "newPasswordConfirm", required = false) String newPasswordConfirm, RedirectAttributes ra) throws UnsupportedEncodingException {

        log.info("Submit change password ...");

        JSONObject user = getLoggedInUser();

        if ((user != null) && DefaultUser.USER_ID.equals(user.get("id"))) {
            log.warn("Change password is not supported for default user, redirect ...");
            return redirect(getDefaultRedirect());
        }
        
        ActionResult result = changePassword(CommonUtils.map(
                "oldPassword", oldPassword, 
                "newPassword", newPassword, 
                "newPasswordConfirm", newPassword, 
                "id", user.get("id")));
        
        if (result.isSuccess()) {
            ra.addFlashAttribute("logout", true);
            ra.addFlashAttribute(ModelKeyConstants.NOTIFY, UIHelper.createSuccessNotification(result.getMessage()));
            return redirect("/user/change-password", ra);
        }
        
        ra.addFlashAttribute("oldPassword", oldPassword);
        ra.addFlashAttribute("newPassword", newPassword);
        ra.addFlashAttribute("newPasswordConfirm", newPasswordConfirm);
        ra.addFlashAttribute(ModelKeyConstants.NOTIFY, UIHelper.createErrorNotification(result.getMessage()));
        
        return redirect("/user/change-password", ra);
    }

    @GetMapping("/user/edit-profile")
    public ModelAndView editProfile(ModelMap modelMap) {
        log.info("Edit profile page ...");
        
        JSONObject user = getLoggedInUser();

        if ((user != null) && DefaultUser.USER_ID.equals(user.get("id"))) {
            log.warn("Edit profile is not supported for default user, redirect ...");
            return redirect(getDefaultRedirect());
        }
        
        if ((user != null) && modelMap.isEmpty()) {
            modelMap.put("fullName", user.get("fullName"));
            modelMap.put("username", user.get("username"));
            modelMap.put("email", user.get("email"));       
        }
        return view("user/edit-profile", modelMap);
    }

    @SuppressWarnings("unchecked")
    @PostMapping(value = "/user/edit-profile")
    public ModelAndView editProfile(
            @RequestParam(name = "fullName", required = true, defaultValue = StringConstants.EMPTY) String fullName,
            @RequestParam(name = "username", required = true, defaultValue = StringConstants.EMPTY) String username,
            @RequestParam(name = "email", required = true, defaultValue = StringConstants.EMPTY) String email,
            @RequestParam(name = "avatar", required = true, defaultValue = StringConstants.EMPTY) MultipartFile avatar, 
            RedirectAttributes ra) throws UnsupportedEncodingException {

        log.info("Submit edit profile ...");

        JSONObject userGroup = getLoggedInUserGroup();
        JSONObject user = getLoggedInUser();

        if ((user != null) && DefaultUser.USER_ID.equals(user.get("id"))) {
            log.warn("Edit profile is not supported for default user, redirect ...");
            return redirect(getDefaultRedirect());
        }

        ra.addFlashAttribute("fullName", fullName);
        ra.addFlashAttribute("username", username);
        ra.addFlashAttribute("email", email);
        
        // Check if submitted username and email equal to default user's username
        for (Object key : usersProperties.keySet()) {
            if (username.equalsIgnoreCase(key.toString()) || email.equalsIgnoreCase(key.toString())) {
                return redirectAndNotifyError("/user/edit-profile", ra, messageHelper.getMessage("error.usernamenotallowed"));
            }
        }

        log.debug("Upload avatar ...");
        String avatarName = System.currentTimeMillis() + StringConstants.UNDERSCORE + RandomStringUtils.randomAlphanumeric(16).toUpperCase() + MultipartFileUtils.getFileExtension(avatar);
        int uploadSize = MultipartFileUtils.upload(avatar, applicationProperties.getProperty("directory.path.images") + "/avatar", avatarName);
        boolean isUploadSuccess = uploadSize > 0;
        
        log.debug("Upload success: {}", isUploadSuccess); 
        
        Map<String, Object> params = new HashMap<>();
        params.put("fullName", fullName.trim());
        params.put("username", username.trim());
        params.put("email", email.trim());
        params.put("password", StringConstants.EMPTY);
        params.put("active", CommonConstants.YES);
        params.put("avatar", isUploadSuccess ? avatarName : StringConstants.EMPTY);
        params.put("userGroupId", userGroup.get("id"));
        params.put("id", user.get("id"));

        log.debug("Params: {}", params);
        
        ActionResult result = editUser(params);        
        
        if (result.isSuccess()) {
            user.put("avatar", isUploadSuccess ? avatarName : user.get("avatar"));
            user.put("fullName", fullName);
            user.put("username", username);
            user.put("email", email);
            SessionManager.set(SessionKeyConstants.USER, user);
            String messageCode = "success.editprofile" + (isUploadSuccess ? "" : ".withoutavatar");
            return redirectAndNotifySuccess("/user/edit-profile", ra, messageHelper.getMessage(messageCode));
        }
        
        return redirectAndNotifyError("/user/edit-profile", ra, result.getMessage());
    }
    
}
