/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.api.controller;

import static ga.classi.api.constant.RequestMappingConstants.USER_ADD;
import static ga.classi.api.constant.RequestMappingConstants.USER_CHANGE_PASSWORD;
import static ga.classi.api.constant.RequestMappingConstants.USER_EDIT;
import static ga.classi.api.constant.RequestMappingConstants.USER_FIND;
import static ga.classi.api.constant.RequestMappingConstants.USER_LIST;
import static ga.classi.api.constant.RequestMappingConstants.USER_LOGIN;
import static ga.classi.api.constant.RequestMappingConstants.USER_REMOVE;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ga.classi.api.helper.ResponseObject;
import ga.classi.commons.constant.RequestDataConstants;
import ga.classi.commons.data.DTO;
import ga.classi.commons.data.utility.DTOUtils;
import ga.classi.commons.web.utility.RequestInformation;
import ga.classi.data.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = USER_LOGIN, method = RequestMethod.POST)
    public ResponseObject login() throws UnsupportedEncodingException {
        RequestInformation reqInfo = new RequestInformation(request);
        DTO dtoLogin = DTOUtils.fromServletRequest(request);
        dtoLogin.put(RequestDataConstants.IP_ADDRESS, reqInfo.getClientIpAddr());
        dtoLogin.put(RequestDataConstants.USER_AGENT, reqInfo.getUserAgent());
        DTO dtoUser = userService.login(dtoLogin);
        return ResponseObject.success(dtoUser);
    }

    @RequestMapping(value = USER_LIST, method = RequestMethod.GET)
    public ResponseObject getAllUser() throws UnsupportedEncodingException {
        log.info("Get all user ...");
        DTO result = userService.getAll(DTOUtils.fromServletRequest(request));
        return ResponseObject.success(result);
    }

    @RequestMapping(value = USER_ADD, method = RequestMethod.POST)
    public ResponseObject addUser() throws UnsupportedEncodingException {
        DTO added = userService.add(DTOUtils.fromServletRequest(request));
        return ResponseObject.success(getResponseMessage("success.SuccessfullyAddUser"), added);
    }

    @RequestMapping(value = USER_REMOVE, method = RequestMethod.POST)
    public ResponseObject removeUser() throws UnsupportedEncodingException {
        userService.remove(DTOUtils.fromServletRequest(request));
        return ResponseObject.success(getResponseMessage("success.SuccessfullyRemoveUser"));
    }

    @RequestMapping(value = USER_FIND, method = RequestMethod.GET)
    public ResponseObject getUser() throws UnsupportedEncodingException {
        DTO user = userService.getOne(DTOUtils.fromServletRequest(request));
        return ResponseObject.success(user);
    }

    @RequestMapping(value = USER_EDIT, method = RequestMethod.POST)
    public ResponseObject editUser() throws UnsupportedEncodingException {
        DTO updated = userService.edit(DTOUtils.fromServletRequest(request));
        return ResponseObject.success(getResponseMessage("success.SuccessfullyEditUser"), updated);
    }

    @PostMapping(USER_CHANGE_PASSWORD)
    public ResponseObject changePassword() throws UnsupportedEncodingException {
        DTO dtoUser = userService.changePassword(DTOUtils.fromServletRequest(request));
        return ResponseObject.success(dtoUser);
    }
    
}
