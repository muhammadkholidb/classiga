package ga.classi.web.controller;

import ga.classi.commons.helper.CommonConstants;
import ga.classi.commons.helper.HttpClient;
import ga.classi.commons.helper.HttpClientResponse;
import ga.classi.commons.helper.StringConstants;
import ga.classi.web.helper.JSONHelper;
import ga.classi.web.helper.ModelKeyConstants;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


/**
 *
 * @author eatonmunoz
 */
@Controller
public class UserGroupController extends HttpClientBaseController {

    private static final Logger log = LoggerFactory.getLogger(UserGroupController.class);

    @GetMapping(value = "/settings/user-group")
    public ModelAndView index() throws IOException {
        log.info("Index ...");
        return view("user-group/list");
    }

    @PostMapping(value = "/settings/user-group/remove")
    public ModelAndView remove(@RequestParam(name = "selected", required = false) String[] selected) throws IOException {

        if (selected == null || selected.length == 0) {
            return redirect("/settings/user-group");
        }
        
        HttpClient httpClient = getDefinedHttpClient("/settings/user-group/remove");

        httpClient.addParameter("id", Arrays.toString(selected));

        HttpClientResponse response = httpClient.post();

        if (CommonConstants.SUCCESS.equals(response.getStatus())) {

            return redirectAndNotifySuccess("/settings/user-group", response.getMessage());

        } else {    // Fail or error

            return redirectAndNotifyError("/settings/user-group", response.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/settings/user-group/add", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView add(
            @RequestParam(name = "name", required = true, defaultValue = StringConstants.EMPTY) String name,
            @RequestParam(name = "description", required = true, defaultValue = StringConstants.EMPTY) String description,
            @RequestParam(name = "menuPermissions", required = true, defaultValue = StringConstants.EMPTY) String encodedMenuPermissions) throws IOException {

        JSONArray availableMenus = (JSONArray) JSONValue.parse(getMenus(true).toString());
        
        List<JSONObject> menus = new ArrayList<JSONObject>();
        for (Object ob : availableMenus) {
            JSONObject menu = JSONHelper.remove((JSONObject) ob, "label", "faIcon", "path", "sequence");
            menu.put("canView", false);
            menu.put("canModify", false);
            menus.add(menu);
        }
        
        String decodedMenuPermissions = URLDecoder.decode(encodedMenuPermissions, "UTF-8");
        
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

        if (isPost()) {

            JSONObject jsonUserGroup = new JSONObject();
            jsonUserGroup.put("name", name.trim());
            jsonUserGroup.put("description", description.trim());
            jsonUserGroup.put("active", CommonConstants.YES);

            JSONObject parameters = new JSONObject();
            parameters.put("userGroup", jsonUserGroup);
            parameters.put("menuPermissions", decodedMenuPermissions);
            
            HttpClientResponse response = getDefinedHttpClient("/settings/user-group/add", parameters).post();

            if (CommonConstants.SUCCESS.equals(response.getStatus())) {
                return redirectAndNotifySuccess("/settings/user-group", response.getMessage());
            } else {    // Fail or error
                return viewAndNotifyError("user-group/form-add", model, response.getMessage());
            }
        }

        return view("user-group/form-add", model);
    }

}
