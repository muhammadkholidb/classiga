/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.web.controller.base;

import ga.classi.commons.helper.ActionResult;
import java.util.Map;

/**
 *
 * @author muhammad
 */
public interface IBaseControllerEmailQueue {

    ActionResult addEmailQueue(Map<String, Object> parameters);
    
}
