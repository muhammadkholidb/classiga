package ga.classi.rest.controller.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ga.classi.commons.data.helper.DTO;
import ga.classi.commons.data.helper.DTOUtils;
import ga.classi.commons.helper.CommonConstants;
import ga.classi.data.service.UserGroupService;
import ga.classi.rest.controller.BaseController;
import ga.classi.rest.helper.ResponseObject;
import java.io.UnsupportedEncodingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/settings")
public class UserGroupController extends BaseController {

    @Autowired
    private UserGroupService userGroupService;

    @RequestMapping(value = "/user-group/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject getAllUserGroup() throws UnsupportedEncodingException  {
        log.info("Get all user group ...");
        DTO result = userGroupService.getAll(DTOUtils.fromServletRequest(request));

        return new ResponseObject(CommonConstants.SUCCESS, result);
    }

    @RequestMapping(value = "/user-group/find", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject getUserGroup() throws UnsupportedEncodingException {

        DTO dto = userGroupService.getOneWithMenuPermissions(DTOUtils.fromServletRequest(request));

        return new ResponseObject(CommonConstants.SUCCESS, dto);
    }

    @RequestMapping(value = "/user-group/remove", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject removeUserGroup() throws UnsupportedEncodingException {

        userGroupService.remove(DTOUtils.fromServletRequest(request));

        return new ResponseObject(CommonConstants.SUCCESS, getResponseMessage("success.SuccessfullyRemoveUserGroup"));
    }

    @RequestMapping(value = "/user-group/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject addUserGroup() throws UnsupportedEncodingException {

        DTO added = userGroupService.add(DTOUtils.fromServletRequest(request));

        return new ResponseObject(CommonConstants.SUCCESS, getResponseMessage("success.SuccessfullyAddUserGroup"), added);
    }

    @RequestMapping(value = "/user-group/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject editUserGroup() throws UnsupportedEncodingException {

        DTO updated = userGroupService.edit(DTOUtils.fromServletRequest(request));

        return new ResponseObject(CommonConstants.SUCCESS, getResponseMessage("success.SuccessfullyEditUserGroup"), updated);
    }

}
