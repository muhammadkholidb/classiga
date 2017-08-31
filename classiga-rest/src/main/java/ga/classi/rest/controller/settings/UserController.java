package ga.classi.rest.controller.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ga.classi.commons.helper.CommonConstants;
import ga.classi.data.helper.Dto;
import ga.classi.data.helper.DtoUtils;
import ga.classi.data.service.UserService;
import ga.classi.rest.controller.BaseController;
import ga.classi.rest.helper.ResponseObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/settings")
public class UserController extends BaseController {
    
    @Autowired
    private UserService userService;

    /**
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/user/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject getAllUser() throws Exception {
        log.info("Get all user ...");
        
        Dto result = userService.getAllUserWithGroup(DtoUtils.fromServletRequest(request)); 
        
        return new ResponseObject(CommonConstants.SUCCESS, result);
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/user/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject addUser() throws Exception {
        
        Dto added = userService.addUser(DtoUtils.fromServletRequest(request));
        
        return new ResponseObject(CommonConstants.SUCCESS, getResponseMessage("success.SuccessfullyAddUser"), added);
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/user/remove", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject removeUser() throws Exception {
        
        userService.removeUser(DtoUtils.fromServletRequest(request));
        
        return new ResponseObject(CommonConstants.SUCCESS, getResponseMessage("success.SuccessfullyRemoveUser"));
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/user/find", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject getUser() throws Exception {
        
        Dto user = userService.getUserById(DtoUtils.fromServletRequest(request));
        
        return new ResponseObject(CommonConstants.SUCCESS, user);
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/user/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject editUser() throws Exception {
        
        Dto updated = userService.editUser(DtoUtils.fromServletRequest(request));
        
        return new ResponseObject(CommonConstants.SUCCESS, getResponseMessage("success.SuccessfullyEditUser"), updated);
    }

}
