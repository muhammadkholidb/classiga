package ga.classi.web.controller.base;

import java.util.Map;

import ga.classi.commons.helper.ActionResult;

/**
 *
 * @author eatonmunoz
 */
public interface IBaseControllerUserGroup {
    
    ActionResult listUserGroup(Map<String, Object> parameters);

    ActionResult findUserGroup(Map<String, Object> parameters);
    
    ActionResult addUserGroup(Map<String, Object> parameters);
    
    ActionResult editUserGroup(Map<String, Object> parameters);
    
    ActionResult removeUserGroup(Map<String, Object> parameters);
    
}
