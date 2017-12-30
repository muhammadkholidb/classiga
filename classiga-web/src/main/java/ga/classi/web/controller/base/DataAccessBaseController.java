package ga.classi.web.controller.base;

import java.io.InputStream;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;

import ga.classi.commons.data.error.DataException;
import ga.classi.commons.data.helper.Dto;
import ga.classi.commons.helper.ActionResult;
import ga.classi.commons.helper.CommonConstants;
import ga.classi.commons.helper.CommonUtils;
import ga.classi.data.helper.DataImporter;
import ga.classi.data.service.SystemService;
import ga.classi.data.service.UserGroupService;
import ga.classi.data.service.UserService;
import ga.classi.web.helper.SessionKeyConstants;
import ga.classi.web.helper.SessionManager;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author eatonmunoz
 */
@Slf4j
public class DataAccessBaseController extends AbstractBaseController implements IBaseController {
    
    @Autowired
    private SystemService systemService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private UserGroupService userGroupService;
    
    @Autowired
    private DataImporter dataImporter;
    
    @Override
    protected void postConstruct() {
        
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void loadSystems() {
        log.debug("Load systems to session ...");
        try {
            
            Dto dto = systemService.getAllSystem(new Dto());
            
            // Import default system data when no data returned
            if ((dto == null) || dto.isEmpty() || dto.get(CommonConstants.CONTENT) == null || ((List) dto.get(CommonConstants.CONTENT)).isEmpty() ) {
                
                log.debug("System data is empty, load initial data ...");
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("dataset/system.xml");
                dataImporter.addStreamDataSet(is);
                dataImporter.importAll();
                
                // Find again
                dto = systemService.getAllSystem(new Dto());
            }

            SessionManager.set(SessionKeyConstants.SYSTEMS, JSONValue.parse(JSONArray.toJSONString((List) dto.get(CommonConstants.CONTENT))));
            updateLocale(getSystem(CommonConstants.SYSTEM_KEY_LANGUAGE_CODE));
            
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ActionResult editSystems(JSONObject parameters, String languageCode) {
        try {
            Dto dto = systemService.editSystemList(new Dto(parameters));
            updateLocale(languageCode);
            return successActionResult(createMessage("success.SuccessfullyEditSystem"), dto.toJSONObject());
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ActionResult login(JSONObject parameters) {
        try {
            Dto dto = userService.login(new Dto(parameters));
            return successActionResult(dto.toJSONObject());
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ActionResult listUser(JSONObject parameters) {
        try {
            Dto dto = userService.getAllUserWithGroup(new Dto(parameters));
            return successActionResult(dto.toJSONObject());
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ActionResult findUser(JSONObject parameters) {
        try {
            Dto dto = userService.getUserById(new Dto(parameters));
            return successActionResult(dto.toJSONObject());
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ActionResult addUser(JSONObject parameters) {
        try {
            Dto dto = userService.addUser(new Dto(parameters));
            return successActionResult(createMessage("success.SuccessfullyAddUser"), dto.toJSONObject());
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ActionResult editUser(JSONObject parameters) {
        try {
            Dto dto = userService.editUser(new Dto(parameters));
            return successActionResult(createMessage("success.SuccessfullyEditUser"), dto.toJSONObject());
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ActionResult removeUser(JSONObject parameters) {
        try {
            userService.removeUser(new Dto(parameters));
            return successActionResult(createMessage("success.SuccessfullyRemoveUser"), new JSONObject());
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ActionResult listUserGroup(JSONObject parameters) {
        try {
            Dto dto = userGroupService.getAllUserGroups(new Dto(parameters));
            return successActionResult(dto.toJSONObject());
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ActionResult findUserGroup(JSONObject parameters) {
        try {
            Dto dto = userGroupService.getOneUserGroupWithMenuPermissions(new Dto(parameters));
            return successActionResult(dto.toJSONObject());
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ActionResult addUserGroup(JSONObject parameters) {
        try {
            Dto dto = userGroupService.addUserGroup(new Dto(parameters));
            return successActionResult(createMessage("success.SuccessfullyAddUserGroup"), dto.toJSONObject());
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ActionResult editUserGroup(JSONObject parameters) {
        try {
            Dto dto = userGroupService.editUserGroup(new Dto(parameters));
            return successActionResult(createMessage("success.SuccessfullyEditUserGroup"), dto.toJSONObject());
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ActionResult removeUserGroup(JSONObject parameters) {
        try {
            userGroupService.removeUserGroup(new Dto(parameters));
            return successActionResult(createMessage("success.SuccessfullyAddUserGroup"), new JSONObject());
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

}
