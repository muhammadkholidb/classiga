package ga.classi.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ga.classi.web.helper.ModelKeyConstants;

@Controller
public class ErrorController extends HttpClientBaseController {

    @RequestMapping(value = "/error")
    public ModelAndView error() {

        int errorCode = httpServletResponse.getStatus();

        Map<String, Object> model = new HashMap<String, Object>();
        model.put(ModelKeyConstants.ERROR_CODE, errorCode);
        model.put(ModelKeyConstants.ERROR_MESSAGE, messageHelper.getMessage("error." + errorCode));
        
        return view("error", model);
    }

}