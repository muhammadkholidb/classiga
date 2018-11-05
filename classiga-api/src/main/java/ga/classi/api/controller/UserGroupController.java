/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.api.controller;

import static ga.classi.api.constant.RequestMappingConstants.USER_GROUP_ADD;
import static ga.classi.api.constant.RequestMappingConstants.USER_GROUP_EDIT;
import static ga.classi.api.constant.RequestMappingConstants.USER_GROUP_FIND;
import static ga.classi.api.constant.RequestMappingConstants.USER_GROUP_LIST;
import static ga.classi.api.constant.RequestMappingConstants.USER_GROUP_REMOVE;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ga.classi.api.helper.ResponseObject;
import ga.classi.commons.constant.CommonConstants;
import ga.classi.commons.data.DTO;
import ga.classi.commons.data.utility.DTOUtils;
import ga.classi.data.service.UserGroupService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class UserGroupController extends BaseController {

    @Autowired
    private UserGroupService userGroupService;

    @RequestMapping(value = USER_GROUP_LIST, method = RequestMethod.GET)
    public ResponseObject getAllUserGroup() throws UnsupportedEncodingException  {
        log.info("Get all user group ...");
        DTO result = userGroupService.getAll(DTOUtils.fromServletRequest(request));
        return ResponseObject.success(result);
    }

    @RequestMapping(value = USER_GROUP_FIND, method = RequestMethod.GET)
    public ResponseObject getUserGroup() throws UnsupportedEncodingException {
        DTO dto = userGroupService.getOneWithMenuPermissions(DTOUtils.fromServletRequest(request));
        return ResponseObject.success(CommonConstants.SUCCESS, dto);
    }

    @RequestMapping(value = USER_GROUP_REMOVE, method = RequestMethod.POST)
    public ResponseObject removeUserGroup() throws UnsupportedEncodingException {
        userGroupService.remove(DTOUtils.fromServletRequest(request));
        return ResponseObject.success(getResponseMessage("success.SuccessfullyRemoveUserGroup"));
    }

    @RequestMapping(value = USER_GROUP_ADD, method = RequestMethod.POST)
    public ResponseObject addUserGroup() throws UnsupportedEncodingException {
        DTO added = userGroupService.add(DTOUtils.fromServletRequest(request));
        return ResponseObject.success(getResponseMessage("success.SuccessfullyAddUserGroup"), added);
    }

    @RequestMapping(value = USER_GROUP_EDIT, method = RequestMethod.POST)
    public ResponseObject editUserGroup() throws UnsupportedEncodingException {
        DTO updated = userGroupService.edit(DTOUtils.fromServletRequest(request));
        return ResponseObject.success(getResponseMessage("success.SuccessfullyEditUserGroup"), updated);
    }

}
