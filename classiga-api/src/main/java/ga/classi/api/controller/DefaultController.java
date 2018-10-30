/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.api.controller;

import java.io.UnsupportedEncodingException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ga.classi.api.helper.ResponseObject;
import ga.classi.commons.constant.CommonConstants;
import ga.classi.commons.constant.StringConstants;
import ga.classi.commons.data.DTO;
import ga.classi.commons.data.utility.DTOUtils;
import ga.classi.data.service.UserService;


@RestController
public class DefaultController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseObject login() throws UnsupportedEncodingException {
        DTO dtoUser = userService.login(DTOUtils.fromServletRequest(request));
        return new ResponseObject(CommonConstants.SUCCESS, dtoUser);
    }

    @GetMapping("/ping")
    public ResponseObject ping() {
        return new ResponseObject(CommonConstants.SUCCESS, StringConstants.EMPTY, Collections.EMPTY_MAP);
    }
    
    @PostMapping("/user/change-password")
    public ResponseObject changePassword() throws UnsupportedEncodingException {
        DTO dtoUser = userService.changePassword(DTOUtils.fromServletRequest(request));
        return new ResponseObject(CommonConstants.SUCCESS, dtoUser);
    }
    
}
