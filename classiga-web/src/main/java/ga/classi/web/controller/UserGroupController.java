package ga.classi.web.controller;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


/**
 *
 * @author eatonmunoz
 */
@Controller
public class UserGroupController extends HttpClientBaseController {

    private static final Logger log = LoggerFactory.getLogger(UserGroupController.class);

    @GetMapping(value = "/settings/user-group")
    public ModelAndView index() throws IOException {
        log.info("Index ...");
        return view("usergroup/list");
    }

}
