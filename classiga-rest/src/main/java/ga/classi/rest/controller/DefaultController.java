package ga.classi.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ga.classi.commons.data.helper.DTO;
import ga.classi.commons.data.helper.DTOUtils;
import ga.classi.commons.helper.CommonConstants;
import ga.classi.commons.helper.StringConstants;
import ga.classi.data.service.UserService;
import ga.classi.rest.helper.ResponseObject;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class DefaultController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject login() throws UnsupportedEncodingException {
        
        DTO dtoUser = userService.login(DTOUtils.fromServletRequest(request));

        return new ResponseObject(CommonConstants.SUCCESS, dtoUser);
    }

    @GetMapping("/ping")
    public ResponseObject ping() {
        return new ResponseObject(CommonConstants.SUCCESS, StringConstants.EMPTY, Collections.EMPTY_MAP);
    }
    
}
