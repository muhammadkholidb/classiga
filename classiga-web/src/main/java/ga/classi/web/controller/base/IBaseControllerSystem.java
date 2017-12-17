package ga.classi.web.controller.base;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import ga.classi.commons.helper.ActionResult;

/**
 *
 * @author eatonmunoz
 */
public interface IBaseControllerSystem {

    JSONArray getSystems();
    
    String getSystem(String key);
    
    void loadSystems();

    ActionResult editSystems(JSONObject parameters, String languageCode) throws IOException;
    
}
