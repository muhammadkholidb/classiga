package ga.classi.web.controller;

import ga.classi.commons.helper.CommonConstants;
import ga.classi.commons.helper.HttpClient;
import ga.classi.commons.helper.HttpClientResponse;
import java.io.IOException;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping(value = "/settings/user-group/remove")
    public ModelAndView remove(@RequestParam(name = "selected", required = false) String[] selected) throws IOException {

        if (selected == null || selected.length == 0) {
            return redirect("/settings/user-group");
        }
        
        HttpClient httpClient = getDefinedHttpClient("/settings/user-group/remove");

        httpClient.addParameter("id", Arrays.toString(selected));

        HttpClientResponse response = httpClient.post();

        if (CommonConstants.SUCCESS.equals(response.getStatus())) {

            return redirectAndNotifySuccess("/settings/user-group", response.getMessage());

        } else {    // Fail or error

            return redirectAndNotifyError("/settings/user-group", response.getMessage());
        }
    }

}
