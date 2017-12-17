package ga.classi.web.controller.base;

import java.io.IOException;

import org.json.simple.JSONObject;

import ga.classi.commons.helper.ActionResult;

/**
 *
 * @author eatonmunoz
 */
public interface IBaseControllerUserGroup {
    
    ActionResult listUserGroup(JSONObject parameters) throws IOException;

    ActionResult findUserGroup(JSONObject parameters) throws IOException;
    
    ActionResult addUserGroup(JSONObject parameters) throws IOException;
    
    ActionResult editUserGroup(JSONObject parameters) throws IOException;
    
    ActionResult removeUserGroup(JSONObject parameters) throws IOException;
    
}
