package ga.classi.rest.controller.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ga.classi.commons.data.helper.Dto;
import ga.classi.commons.data.helper.DtoUtils;
import ga.classi.commons.helper.CommonConstants;
import ga.classi.data.service.UserGroupService;
import ga.classi.rest.controller.BaseController;
import ga.classi.rest.helper.ResponseObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/settings")
public class UserGroupController extends BaseController {

    @Autowired
    private UserGroupService userGroupService;

    @RequestMapping(value = "/user-group/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject getAllUserGroup()  throws Exception {
        log.info("Get all user group ...");
        Dto result = userGroupService.getAllUserGroups(DtoUtils.fromServletRequest(request));

        return new ResponseObject(CommonConstants.SUCCESS, result);
    }

    @RequestMapping(value = "/user-group/find", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject getUserGroup() throws Exception {

        Dto dto = userGroupService.getOneUserGroupWithMenuPermissions(DtoUtils.fromServletRequest(request));

        return new ResponseObject(CommonConstants.SUCCESS, dto);
    }

    @RequestMapping(value = "/user-group/remove", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject removeUserGroup() throws Exception {

        userGroupService.removeUserGroup(DtoUtils.fromServletRequest(request));

        return new ResponseObject(CommonConstants.SUCCESS, getResponseMessage("success.SuccessfullyRemoveUserGroup"));
    }

    @RequestMapping(value = "/user-group/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject addUserGroup() throws Exception {

        Dto added = userGroupService.addUserGroup(DtoUtils.fromServletRequest(request));

        return new ResponseObject(CommonConstants.SUCCESS, getResponseMessage("success.SuccessfullyAddUserGroup"), added);
    }

    @RequestMapping(value = "/user-group/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject editUserGroup() throws Exception {

        Dto updated = userGroupService.editUserGroup(DtoUtils.fromServletRequest(request));

        return new ResponseObject(CommonConstants.SUCCESS, getResponseMessage("success.SuccessfullyEditUserGroup"), updated);
    }

}
