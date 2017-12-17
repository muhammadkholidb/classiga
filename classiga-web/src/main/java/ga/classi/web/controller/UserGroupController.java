package ga.classi.web.controller;

import java.io.IOException;
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
import ga.classi.commons.helper.CommonConstants;
import ga.classi.commons.helper.StringConstants;
import ga.classi.web.controller.base.BaseControllerAdapter;
import ga.classi.web.helper.JSONHelper;
import ga.classi.web.helper.ModelKeyConstants;
import lombok.extern.slf4j.Slf4j;


/**
 *
 * @author eatonmunoz
 */
@Slf4j
@Controller
public class UserGroupController extends BaseControllerAdapter {

    @GetMapping(value = "/settings/user-group")
    public ModelAndView index() throws IOException {
        log.info("Index ...");
        return view("user-group/list");
    }

    @SuppressWarnings("unchecked")
    @PostMapping(value = "/settings/user-group/remove")
    public ModelAndView remove(@RequestParam(name = "selected", required = false) String[] selected) throws IOException {

        if (selected == null || selected.length == 0) {
            return redirect("/settings/user-group");
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
            @RequestParam(name = "menuPermissions", required = true, defaultValue = StringConstants.EMPTY) String encodedMenuPermissions) throws IOException {

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

        JSONArray availableMenus = (JSONArray) JSONValue.parse(getMenus(true).toString());
        
        List<JSONObject> menus = new ArrayList<JSONObject>();
        for (Object ob : availableMenus) {
            JSONObject menu = JSONHelper.remove((JSONObject) ob, "label", "faIcon", "path", "sequence");
            menu.put("canView", false);
            menu.put("canModify", false);
            menus.add(menu);
        }
        
        if (!StringConstants.EMPTY.equals(decodedMenuPermissions)) {
            JSONArray submittedMenuPermissions = (JSONArray) JSONValue.parse(decodedMenuPermissions);
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
            }
        }

        Map<String, Object> model = new HashMap<String, Object>();
        model.put(ModelKeyConstants.NAME, name);
        model.put(ModelKeyConstants.DESCRIPTION, description);
        model.put(ModelKeyConstants.MENU_PERMISSIONS, menus);

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
            @RequestParam(name = "active", required = true, defaultValue = CommonConstants.NO) String active) throws IOException {

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
                return redirectAndNotifySuccess("/settings/user-group", result.getMessage());
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

        JSONArray availableMenus = (JSONArray) JSONValue.parse(getMenus(true).toString());
        
        List<JSONObject> menus = new ArrayList<JSONObject>();
        for (Object ob : availableMenus) {
            JSONObject menu = JSONHelper.remove((JSONObject) ob, "label", "faIcon", "path", "sequence");
            menu.put("canView", false);
            menu.put("canModify", false);
            menus.add(menu);
        }
        
        if (!StringConstants.EMPTY.equals(decodedMenuPermissions)) {
            JSONArray submittedMenuPermissions = (JSONArray) JSONValue.parse(decodedMenuPermissions);
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
            }
        } else if (userGroupMenuPermissions != null) {
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
        }

        if (errorMessage != null) {
            
            Map<String, Object> model = new HashMap<String, Object>();
            model.put(ModelKeyConstants.USER_GROUP_ID, userGroupId);
            model.put(ModelKeyConstants.NAME, name);
            model.put(ModelKeyConstants.DESCRIPTION, description);
            model.put(ModelKeyConstants.MENU_PERMISSIONS, menus);
            model.put(ModelKeyConstants.ACTIVE, active);

            return viewAndNotifyError("user-group/form-edit", model, errorMessage);
        }

        Map<String, Object> model = new HashMap<String, Object>();
        model.put(ModelKeyConstants.USER_GROUP_ID, userGroup.get("id"));
        model.put(ModelKeyConstants.NAME, userGroup.get("name"));
        model.put(ModelKeyConstants.DESCRIPTION, userGroup.get("description"));
        model.put(ModelKeyConstants.MENU_PERMISSIONS, menus);
        model.put(ModelKeyConstants.ACTIVE, userGroup.get("active"));

        return view("user-group/form-edit", model);
    }

}
