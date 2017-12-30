package ga.classi.web.controller.base;

import org.json.simple.JSONObject;

import ga.classi.commons.helper.ActionResult;

/**
 *
 * @author eatonmunoz
 */
public interface IBaseControllerUser {

    boolean userHasLoggedIn();
    
    ActionResult login(JSONObject parameters);
    
    ActionResult listUser(JSONObject parameters);

    ActionResult findUser(JSONObject parameters);
    
    ActionResult addUser(JSONObject parameters);
    
    ActionResult editUser(JSONObject parameters);
    
    ActionResult removeUser(JSONObject parameters);
    
}
