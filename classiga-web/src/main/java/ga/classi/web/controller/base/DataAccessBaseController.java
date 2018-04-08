package ga.classi.web.controller.base;

import java.io.InputStream;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;

import ga.classi.commons.data.error.DataException;
import ga.classi.commons.data.helper.DTO;
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
        // Post construct
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void loadSystems() {
        log.debug("Load systems to session ...");
        try {
            
            DTO dto = systemService.getAllSystem(new DTO());
            
            // Import default system data when no data returned
            if ((dto == null) || dto.isEmpty() || dto.get(CommonConstants.CONTENT) == null || ((List) dto.get(CommonConstants.CONTENT)).isEmpty() ) {
                
                log.debug("System data is empty, load initial data ...");
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("dataset/system.xml");
                dataImporter.addStreamDataSet(is);
                dataImporter.importAll();
                
                // Find again
                dto = systemService.getAllSystem(new DTO());
            }

            SessionManager.set(SessionKeyConstants.SYSTEMS, (JSONArray) JSONValue.parse(JSONArray.toJSONString((List) dto.get(CommonConstants.CONTENT))));
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
            DTO dto = systemService.editSystemList(new DTO(parameters));
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
            DTO dto = userService.login(new DTO(parameters));
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
            DTO dto = userService.getAll(new DTO(parameters));
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
            DTO dto = userService.getOne(new DTO(parameters));
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
            DTO dto = userService.add(new DTO(parameters));
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
            DTO dto = userService.edit(new DTO(parameters));
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
            userService.remove(new DTO(parameters));
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
            DTO dto = userGroupService.getAll(new DTO(parameters));
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
            DTO dto = userGroupService.getOneWithMenuPermissions(new DTO(parameters));
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
            DTO dto = userGroupService.add(new DTO(parameters));
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
            DTO dto = userGroupService.edit(new DTO(parameters));
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
            userGroupService.remove(new DTO(parameters));
            return successActionResult(createMessage("success.SuccessfullyRemoveUserGroup"), new JSONObject());
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

}
