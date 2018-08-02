/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.web.controller.base;

import java.util.Map;

import org.json.simple.JSONObject;

import ga.classi.commons.helper.ActionResult;

/**
 *
 * @author muhammad
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
