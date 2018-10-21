/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.web.controller.base;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;

import ga.classi.commons.data.error.DataException;
import ga.classi.commons.data.DTO;
import ga.classi.commons.utility.ActionResult;
import ga.classi.commons.constant.CommonConstants;
import ga.classi.commons.utility.CommonUtils;
import ga.classi.data.helper.DataImporter;
import ga.classi.data.service.SystemService;
import ga.classi.data.service.UserGroupService;
import ga.classi.data.service.UserService;
import ga.classi.web.helper.SessionManager;
import lombok.extern.slf4j.Slf4j;
import ga.classi.web.constant.SessionConstants;

/**
 * 
 * @author muhammad
 */
@Slf4j
public class DataAccessBaseController extends AbstractBaseController implements IBaseController {
    
    @Autowired
    private SystemService    systemService;

    @Autowired
    private UserService      userService;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private DataImporter     dataImporter;

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
            if ((dto == null) || dto.isEmpty() || dto.get(CommonConstants.CONTENT) == null
                    || ((List) dto.get(CommonConstants.CONTENT)).isEmpty()) {

                log.debug("System data is empty, load initial data ...");
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("dataset/system.xml");
                dataImporter.addStreamDataSet(is);
                dataImporter.importAll();

                // Find again
                dto = systemService.getAllSystem(new DTO());
            }

            SessionManager.set(SessionConstants.SYSTEMS,
                    (JSONArray) JSONValue.parse(JSONArray.toJSONString((List) dto.get(CommonConstants.CONTENT))));
            updateLocale(getSystem(CommonConstants.SYSTEM_KEY_LANGUAGE_CODE));

        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
        }
    }

    @Override
    public ActionResult editSystems(Map<String, Object> parameters, String languageCode) {
        try {
            DTO dto = systemService.editSystemList(new DTO(parameters));
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

    @Override
    public ActionResult login(Map<String, Object> parameters) {
        try {
            DTO dto = userService.login(new DTO(parameters));
            return successActionResult(dto);
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult listUser(Map<String, Object> parameters) {
        try {
            DTO dto = userService.getAll(new DTO(parameters));
            return successActionResult(dto);
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult findUser(Map<String, Object> parameters) {
        try {
            DTO dto = userService.getOne(new DTO(parameters));
            return successActionResult(dto);
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult addUser(Map<String, Object> parameters) {
        try {
            DTO dto = userService.add(new DTO(parameters));
            return successActionResult(createMessage("success.SuccessfullyAddUser"), dto);
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult editUser(Map<String, Object> parameters) {
        try {
            DTO dto = userService.edit(new DTO(parameters));
            return successActionResult(createMessage("success.SuccessfullyEditUser"), dto);
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult removeUser(Map<String, Object> parameters) {
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

    @Override
    public ActionResult changePassword(Map<String, Object> parameters) {
        try {
            userService.changePassword(new DTO(parameters));
            return successActionResult(createMessage("success.SuccessfullyChangePassword"), new JSONObject());
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult listUserGroup(Map<String, Object> parameters) {
        try {
            DTO dto = userGroupService.getAll(new DTO(parameters));
            return successActionResult(dto);
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult findUserGroup(Map<String, Object> parameters) {
        try {
            DTO dto = userGroupService.getOneWithMenuPermissions(new DTO(parameters));
            return successActionResult(dto);
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult addUserGroup(Map<String, Object> parameters) {
        try {
            DTO dto = userGroupService.add(new DTO(parameters));
            return successActionResult(createMessage("success.SuccessfullyAddUserGroup"), dto);
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult editUserGroup(Map<String, Object> parameters) {
        try {
            DTO dto = userGroupService.edit(new DTO(parameters));
            return successActionResult(createMessage("success.SuccessfullyEditUserGroup"), dto);
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult removeUserGroup(Map<String, Object> parameters) {
        try {
            userGroupService.remove(new DTO(parameters));
            return successActionResult(createMessage("success.SuccessfullyRemoveUserGroup"), new HashMap<>());
        } catch (DataException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return failActionResult(createMessage(e.getMessage(), e.getData()));
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

}
