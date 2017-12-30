package ga.classi.web.controller.base;

import org.json.simple.JSONObject;

import ga.classi.commons.helper.ActionResult;

/**
 *
 * @author eatonmunoz
 */
public interface IBaseControllerUserGroup {
    
    ActionResult listUserGroup(JSONObject parameters);

    ActionResult findUserGroup(JSONObject parameters);
    
    ActionResult addUserGroup(JSONObject parameters);
    
    ActionResult editUserGroup(JSONObject parameters);
    
    ActionResult removeUserGroup(JSONObject parameters);
    
}
