package ga.classi.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ga.classi.commons.data.helper.Dto;
import ga.classi.commons.data.helper.DtoUtils;
import ga.classi.commons.helper.CommonConstants;
import ga.classi.data.service.UserService;
import ga.classi.rest.helper.ResponseObject;

@RestController
public class DefaultController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject login() throws Exception {
        
        Dto dtoUser = userService.login(DtoUtils.fromServletRequest(request));

        return new ResponseObject(CommonConstants.SUCCESS, dtoUser);
    }

}
