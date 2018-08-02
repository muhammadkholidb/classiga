/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ga.classi.web.controller.base.BaseControllerAdapter;
import ga.classi.web.constant.ModelConstants;

@Controller
public class ErrorController extends BaseControllerAdapter {

    @RequestMapping(value = "/error")
    public ModelAndView error() {

        int errorCode = httpServletResponse.getStatus();

        Map<String, Object> model = new HashMap<String, Object>();
        model.put(ModelConstants.ERROR_CODE, errorCode);
        model.put(ModelConstants.ERROR_MESSAGE, messageHelper.getMessage("error." + errorCode));
        
        return view("error", model);
    }

}