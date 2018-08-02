/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.web.controller.base;

import java.util.Map;

import org.json.simple.JSONArray;

import ga.classi.commons.helper.ActionResult;

/**
 *
 * @author muhammad
 */
public interface IBaseControllerSystem {

    JSONArray getSystems();
    
    String getSystem(String key);
    
    void loadSystems();

    ActionResult editSystems(Map<String, Object> parameters, String languageCode);
    
}
