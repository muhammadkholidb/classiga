package ga.classi.web.controller.base;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;

import ga.classi.commons.data.error.DataException;
import ga.classi.commons.data.helper.Dto;
import ga.classi.commons.helper.ActionResult;
import ga.classi.commons.helper.CommonConstants;
import ga.classi.commons.helper.CommonUtils;
import ga.classi.commons.helper.StringConstants;
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

    private String createMessage(String code, Object[] arguments) {
        Locale locale = localeResolver.resolveLocale(httpServletRequest);
        if (locale != null) {
            return messageHelper.getMessage(code, arguments, locale);
        }
        return messageHelper.getMessage(code, arguments);
    }

    private String createMessage(String code) {
        return createMessage(code, null);
    }

    private ActionResult createActionResult(String status, String message, Dto dto) {
        String strJsonDto = JSONValue.toJSONString(dto);    // Make it string so the value can be parsed as JSON object not ArrayList or Map
        ActionResult result = new ActionResult();
        result.setStatus(status);
        result.setMessage(message);
        result.setData((JSONObject) JSONValue.parse(strJsonDto));
        return result.parseData();
    }
    
    private ActionResult successActionResult(String message, Dto dto) {
        return createActionResult(CommonConstants.SUCCESS, message, dto);
    }

    private ActionResult successActionResult(Dto dto) {
        return successActionResult(StringConstants.EMPTY, dto);
    }
    
    private ActionResult failActionResult(String message) {
        return createActionResult(CommonConstants.FAIL, message, new Dto());
    }

    private ActionResult errorActionResult() {
        return createActionResult(CommonConstants.ERROR, createMessage("error.ServerError"), new Dto());
    }
    
    @SuppressWarnings("rawtypes")
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
    public ActionResult editSystems(JSONObject parameters, String languageCode) throws IOException {
        try {
            Dto dto = systemService.editSystemList(new Dto(parameters));
            updateLocale(languageCode);
            return successActionResult(createMessage("success.SuccessfullyEditSystem"), dto);
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @SuppressWarnings("unchecked")
    public ActionResult login(JSONObject parameters) {
        try {
            Dto dto = userService.login(new Dto(parameters));
            return successActionResult(dto);
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @SuppressWarnings("unchecked")
    public ActionResult listUser(JSONObject parameters) throws IOException {
        try {
            Dto dto = userService.getAllUserWithGroup(new Dto(parameters));
            return successActionResult(dto);
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @SuppressWarnings("unchecked")
    public ActionResult findUser(JSONObject parameters) throws IOException {
        try {
            Dto dto = userService.getUserById(new Dto(parameters));
            return successActionResult(dto);
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @SuppressWarnings("unchecked")
    public ActionResult addUser(JSONObject parameters) throws IOException {
        try {
            Dto dto = userService.addUser(new Dto(parameters));
            return successActionResult(createMessage("success.SuccessfullyAddUser"), dto);
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @SuppressWarnings("unchecked")
    public ActionResult editUser(JSONObject parameters) throws IOException {
        try {
            Dto dto = userService.editUser(new Dto(parameters));
            return successActionResult(createMessage("success.SuccessfullyEditUser"), dto);
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @SuppressWarnings("unchecked")
    public ActionResult removeUser(JSONObject parameters) throws IOException {
        try {
            userService.removeUser(new Dto(parameters));
            return successActionResult(createMessage("success.SuccessfullyRemoveUser"), new Dto());
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @SuppressWarnings("unchecked")
    public ActionResult listUserGroup(JSONObject parameters) throws IOException {
        try {
            Dto dto = userGroupService.getAllUserGroups(new Dto(parameters));
            return successActionResult(dto);
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @SuppressWarnings("unchecked")
    public ActionResult findUserGroup(JSONObject parameters) throws IOException {
        try {
            Dto dto = userGroupService.getOneUserGroupWithMenuPermissions(new Dto(parameters));
            return successActionResult(dto);
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @SuppressWarnings("unchecked")
    public ActionResult addUserGroup(JSONObject parameters) throws IOException {
        try {
            Dto dto = userGroupService.addUserGroup(new Dto(parameters));
            return successActionResult(createMessage("success.SuccessfullyAddUserGroup"), dto);
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @SuppressWarnings("unchecked")
    public ActionResult editUserGroup(JSONObject parameters) throws IOException {
        try {
            Dto dto = userGroupService.editUserGroup(new Dto(parameters));
            return successActionResult(createMessage("success.SuccessfullyEditUserGroup"), dto);
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @SuppressWarnings("unchecked")
    public ActionResult removeUserGroup(JSONObject parameters) throws IOException {
        try {
            userGroupService.removeUserGroup(new Dto(parameters));
            return successActionResult(createMessage("success.SuccessfullyAddUserGroup"), new Dto());
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

}
