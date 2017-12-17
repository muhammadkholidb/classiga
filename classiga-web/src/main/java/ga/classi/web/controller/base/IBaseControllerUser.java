package ga.classi.web.controller.base;

import java.io.IOException;

import org.json.simple.JSONObject;

import ga.classi.commons.helper.ActionResult;

/**
 *
 * @author eatonmunoz
 */
public interface IBaseControllerUser {

    boolean userHasLoggedIn();
    
    ActionResult login(JSONObject parameters) throws IOException;
    
    ActionResult listUser(JSONObject parameters) throws IOException;

    ActionResult findUser(JSONObject parameters) throws IOException;
    
    ActionResult addUser(JSONObject parameters) throws IOException;
    
    ActionResult editUser(JSONObject parameters) throws IOException;
    
    ActionResult removeUser(JSONObject parameters) throws IOException;
    
}
