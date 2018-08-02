/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.web.controller;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ga.classi.commons.helper.ActionResult;
import ga.classi.commons.constant.CommonConstants;
import ga.classi.commons.constant.StringConstants;
import ga.classi.commons.web.helper.JSON;
import ga.classi.web.controller.base.BaseControllerAdapter;
import ga.classi.web.ui.Notify;
import java.io.UnsupportedEncodingException;
import lombok.extern.slf4j.Slf4j;
import ga.classi.web.constant.ModelConstants;


/**
 *
 * @author muhammad
 */
@Slf4j
@Controller
public class UserGroupController extends BaseControllerAdapter {

    @GetMapping(value = "/settings/user-group")
    public ModelAndView index() {
        log.info("Index ...");
        return view("user-group/list");
    }

    @SuppressWarnings("unchecked")
    @PostMapping(value = "/settings/user-group/remove")
    public ModelAndView remove(@RequestParam(name = "selected", required = false) String[] selected) {

        if (selected == null || selected.length == 0) {
            return redirect("/settings/user-group");
        }
        
        JSONObject loggedInUserGroup = getLoggedInUserGroup();
        
        for (String strId : selected) {
            String loggedInUserGroupId = loggedInUserGroup.get("id").toString();
            if (loggedInUserGroupId.equals(strId)) {
                return redirectAndNotifyError("/settings/user-group", messageHelper.getMessage("error.cannotremovecurrenlyloginusergroup"));
            }
        }
        
        JSONObject params = new JSONObject();
        params.put("id", Arrays.toString(selected));

        ActionResult result = removeUserGroup(params);
        
        if (CommonConstants.SUCCESS.equals(result.getStatus())) {

            return redirectAndNotifySuccess("/settings/user-group", result.getMessage());

        } else {    // Fail or error

            return redirectAndNotifyError("/settings/user-group", result.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/settings/user-group/add", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView add(
            @RequestParam(name = "name", required = true, defaultValue = StringConstants.EMPTY) String name,
            @RequestParam(name = "description", required = true, defaultValue = StringConstants.EMPTY) String description,
            @RequestParam(name = "menuPermissions", required = true, defaultValue = StringConstants.EMPTY) String encodedMenuPermissions) throws UnsupportedEncodingException {

        String decodedMenuPermissions = URLDecoder.decode(encodedMenuPermissions, "UTF-8");
        
        String errorMessage = null;

        if (isPost()) {

            JSONObject jsonUserGroup = new JSONObject();
            jsonUserGroup.put("name", name.trim());
            jsonUserGroup.put("description", description.trim());
            jsonUserGroup.put("active", CommonConstants.YES);

            JSONObject parameters = new JSONObject();
            parameters.put("userGroup", jsonUserGroup);
            parameters.put("menuPermissions", decodedMenuPermissions);
            
            ActionResult result = addUserGroup(parameters);

            if (CommonConstants.SUCCESS.equals(result.getStatus())) {
                return redirectAndNotifySuccess("/settings/user-group", result.getMessage());
            } 
            
            // This is not success
            errorMessage = result.getMessage();
        }

        List<JSONObject> menus = loadCheckedMenuPermissions((JSONArray) JSONValue.parse(decodedMenuPermissions), null);            
        
        Map<String, Object> model = new HashMap<>();
        model.put(ModelConstants.NAME, name);
        model.put(ModelConstants.DESCRIPTION, description);
        model.put(ModelConstants.MENU_PERMISSIONS, menus);

        if (errorMessage != null) {
            return viewAndNotifyError("user-group/form-add", model, errorMessage);
        }

        return view("user-group/form-add", model);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/settings/user-group/edit/{userGroupId}", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView edit(
            @PathVariable String userGroupId,
            @RequestParam(name = "name", required = true, defaultValue = StringConstants.EMPTY) String name,
            @RequestParam(name = "description", required = true, defaultValue = StringConstants.EMPTY) String description,
            @RequestParam(name = "menuPermissions", required = true, defaultValue = StringConstants.EMPTY) String encodedMenuPermissions,
            @RequestParam(name = "active", required = true, defaultValue = CommonConstants.NO) String active) throws UnsupportedEncodingException {

        String decodedMenuPermissions = URLDecoder.decode(encodedMenuPermissions, "UTF-8");

        String errorMessage = null;
        
        if (isPost()) {

            JSONObject jsonUserGroup = new JSONObject();
            jsonUserGroup.put("id", userGroupId.trim());
            jsonUserGroup.put("name", name.trim());
            jsonUserGroup.put("description", description.trim());
            jsonUserGroup.put("active", active);

            JSONObject parameters = new JSONObject();
            parameters.put("userGroup", jsonUserGroup);
            parameters.put("menuPermissions", decodedMenuPermissions);
            
            ActionResult result = editUserGroup(parameters);

            if (CommonConstants.SUCCESS.equals(result.getStatus())) {
                JSONObject loggedInUserGroup = getLoggedInUserGroup();
                Map<String, Object> flash = new HashMap<>();
                if (loggedInUserGroup.get("id").toString().equals(userGroupId.trim())) {
                    flash.put("logout", true);
                }
                return redirectAndNotify("/settings/user-group", flash, result.getMessage(), Notify.SUCCESS);
            } 
            
            // This is not success
            errorMessage = result.getMessage();
        }

        JSONObject userGroup;
        JSONArray userGroupMenuPermissions;
        
        JSONObject paramsFind = new JSONObject();
        paramsFind.put("id", userGroupId);

        ActionResult resultFind = findUserGroup(paramsFind);
        
        if (CommonConstants.SUCCESS.equals(resultFind.getStatus())) {
            userGroup = (JSONObject) resultFind.getContent();
            userGroupMenuPermissions = (JSONArray) userGroup.get("menuPermissions");
        } else {    // Fail or error
            return redirectAndNotifyError("/settings/user-group", resultFind.getMessage());
        }

        List<JSONObject> menus = loadCheckedMenuPermissions((JSONArray) JSONValue.parse(decodedMenuPermissions), userGroupMenuPermissions);            
        
        if (errorMessage != null) {
            
            Map<String, Object> model = new HashMap<>();
            model.put(ModelConstants.USER_GROUP_ID, userGroupId);
            model.put(ModelConstants.NAME, name);
            model.put(ModelConstants.DESCRIPTION, description);
            model.put(ModelConstants.MENU_PERMISSIONS, menus);
            model.put(ModelConstants.ACTIVE, active);

            return viewAndNotifyError("user-group/form-edit", model, errorMessage);
        }

        Map<String, Object> model = new HashMap<>();
        model.put(ModelConstants.USER_GROUP_ID, userGroup.get("id"));
        model.put(ModelConstants.NAME, userGroup.get("name"));
        model.put(ModelConstants.DESCRIPTION, userGroup.get("description"));
        model.put(ModelConstants.MENU_PERMISSIONS, menus);
        model.put(ModelConstants.ACTIVE, userGroup.get("active"));

        return view("user-group/form-edit", model);
    }

    @SuppressWarnings("unchecked")
    private List<JSONObject> loadCheckedMenuPermissions(JSONArray submittedMenuPermissions, JSONArray userGroupMenuPermissions) {
        
        JSONArray availableMenus = (JSONArray) JSONValue.parse(getMenus(true).toString());
        
        List<JSONObject> menus = new ArrayList<>();
        
        for (Object ob : availableMenus) {
            JSONObject menu = JSON.remove((JSONObject) ob, "label", "faIcon", "path", "sequence");
            menu.put("canView", false);
            menu.put("canModify", false);
            menus.add(menu);
        }
        
        if (submittedMenuPermissions != null) {
            for (JSONObject menu : menus) {
                for (Object ob : submittedMenuPermissions) {
                    JSONObject submittedMenu = (JSONObject) ob;
                    if (menu.get("code").equals(submittedMenu.get("menuCode"))) {
                        menu.put("canView", CommonConstants.YES.equals(submittedMenu.get("canView")));
                        menu.put("canModify", CommonConstants.YES.equals(submittedMenu.get("canModify")));
                        break;
                    }
                }
            }
            return menus;
        } 
        
        if (userGroupMenuPermissions != null) {
            for (JSONObject menu : menus) {
                for (Object ob : userGroupMenuPermissions) {
                    JSONObject ugMenu = (JSONObject) ob;
                    if (menu.get("code").equals(ugMenu.get("menuCode"))) {
                        menu.put("canView", CommonConstants.YES.equals(ugMenu.get("canView")));
                        menu.put("canModify", CommonConstants.YES.equals(ugMenu.get("canModify")));
                        break;
                    }
                }
            }
            return menus;
        }

        return menus;
    }
    
}
