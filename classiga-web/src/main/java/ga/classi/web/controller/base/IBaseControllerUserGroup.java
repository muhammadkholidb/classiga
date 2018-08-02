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
public interface IBaseControllerUserGroup {

    JSONObject getLoggedInUserGroup();
    
    ActionResult listUserGroup(Map<String, Object> parameters);

    ActionResult findUserGroup(Map<String, Object> parameters);
    
    ActionResult addUserGroup(Map<String, Object> parameters);
    
    ActionResult editUserGroup(Map<String, Object> parameters);
    
    ActionResult removeUserGroup(Map<String, Object> parameters);
    
}
