/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ga.classi.commons.data.DTO;
import ga.classi.commons.data.utility.DTOUtils;
import ga.classi.commons.constant.CommonConstants;
import ga.classi.data.service.UserService;
import ga.classi.api.helper.ResponseObject;
import java.io.UnsupportedEncodingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/settings")
public class UserController extends BaseController {
    
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user/list", method = RequestMethod.GET)
    public ResponseObject getAllUser() throws UnsupportedEncodingException {
        log.info("Get all user ...");
        DTO result = userService.getAll(DTOUtils.fromServletRequest(request)); 
        return new ResponseObject(CommonConstants.SUCCESS, result);
    }

    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public ResponseObject addUser() throws UnsupportedEncodingException {
        DTO added = userService.add(DTOUtils.fromServletRequest(request));
        return new ResponseObject(CommonConstants.SUCCESS, getResponseMessage("success.SuccessfullyAddUser"), added);
    }

    @RequestMapping(value = "/user/remove", method = RequestMethod.POST)
    public ResponseObject removeUser() throws UnsupportedEncodingException {
        userService.remove(DTOUtils.fromServletRequest(request));
        return new ResponseObject(CommonConstants.SUCCESS, getResponseMessage("success.SuccessfullyRemoveUser"));
    }

    @RequestMapping(value = "/user/find", method = RequestMethod.GET)
    public ResponseObject getUser() throws UnsupportedEncodingException {
        DTO user = userService.getOne(DTOUtils.fromServletRequest(request));
        return new ResponseObject(CommonConstants.SUCCESS, user);
    }

    @RequestMapping(value = "/user/edit", method = RequestMethod.POST)
    public ResponseObject editUser() throws UnsupportedEncodingException {
        DTO updated = userService.edit(DTOUtils.fromServletRequest(request));
        return new ResponseObject(CommonConstants.SUCCESS, getResponseMessage("success.SuccessfullyEditUser"), updated);
    }

}
