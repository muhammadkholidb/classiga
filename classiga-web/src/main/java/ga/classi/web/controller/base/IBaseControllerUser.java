package ga.classi.web.controller.base;

import java.util.Map;

import org.json.simple.JSONObject;

import ga.classi.commons.helper.ActionResult;

/**
 *
 * @author eatonmunoz
 */
public interface IBaseControllerUser {

    boolean userHasLoggedIn();
    
    JSONObject getLoggedInUser();
    
    ActionResult login(Map<String, Object> parameters);
    
    ActionResult listUser(Map<String, Object> parameters);

    ActionResult findUser(Map<String, Object> parameters);
    
    ActionResult addUser(Map<String, Object> parameters);
    
    ActionResult editUser(Map<String, Object> parameters);
    
    ActionResult removeUser(Map<String, Object> parameters);
    
    ActionResult changePassword(Map<String, Object> parameters);
    
}
