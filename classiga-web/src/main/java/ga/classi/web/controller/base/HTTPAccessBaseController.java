package ga.classi.web.controller.base;

import java.util.Map;

import org.json.simple.JSONArray;

import ga.classi.commons.helper.ActionResult;
import ga.classi.commons.constant.CommonConstants;
import ga.classi.commons.helper.CommonUtils;
import ga.classi.commons.web.helper.HTTP;
import ga.classi.commons.web.helper.HTTPResponse;
import ga.classi.web.helper.SessionManager;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import ga.classi.web.constant.SessionConstants;

/**
 * 
 * @author eatonmunoz
 */
@Slf4j
public class HTTPAccessBaseController extends AbstractBaseController implements IBaseController {

    protected String hostUrl;

    @Override
    protected void postConstruct() {
        hostUrl = applicationProperties.getProperty("rest.url.host");
    }

    /**
     * Returns {@link HTTP} with host defined in project properties file.
     * @return The HttpClient with predefined configuration.
     */
    protected HTTP defaultHTTP() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept-Language", getSystem(CommonConstants.SYSTEM_KEY_LANGUAGE_CODE));
        HTTP http = new HTTP();
        http.setHeaders(headers);
        return http;
    }

    /**
     * Returns {@link HTTP} with host defined in project properties file for specified path.
     * @param path The path of the API to access.
     * @return The HttpClient with predefined configuration.
     */
    protected HTTP defaultHTTP(String path) {
        HTTP http = defaultHTTP();
        http.setUrl(hostUrl + path); 
        return http;
    }
    
    /**
     * Returns {@link HTTP} with host defined in project properties file for specified path and parameters.
     * @param path The path of the API to access.
     * @param parameters The parameters for the API.
     * @return The HttpClient with predefined configuration.
     */
    protected HTTP defaultHTTP(String path, Map<String, Object> parameters) {
        HTTP http = defaultHTTP(path);
        http.setBody(parameters);
        return http;
    }
    
    @Override
    public void loadSystems() {
        log.debug("Load systems to session ...");
        try {
            // Cannot use method defaultHTTP() because it will add a header 
            // which will call this method. It will cause a forever loop. 
            // Just use a new HTTP()
            HTTP http = new HTTP();
            http.setUrl(hostUrl + "/settings/system/list"); 
            HTTPResponse response = http.get();
            if (response != null) {
                if (CommonConstants.SUCCESS.equals(response.getStatus())) {
                    SessionManager.set(SessionConstants.SYSTEMS, (JSONArray) response.getContent());
                    updateLocale(getSystem(CommonConstants.SYSTEM_KEY_LANGUAGE_CODE));
                } else {    
                    log.error(response.getMessage());
                }
            }
        } catch (IOException e) {
            log.error(CommonUtils.getExceptionMessage(e));
        }
    }

    @Override
    public ActionResult editSystems(Map<String, Object> parameters, String languageCode) {
        try {            
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept-Language", languageCode);
            HTTP http = defaultHTTP("/settings/system/edit", parameters);
            http.setHeaders(headers);
            return http.post();
        } catch (IOException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult login(Map<String, Object> parameters) {   
        try {            
            return defaultHTTP("/login", parameters).post();
        } catch (IOException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult listUser(Map<String, Object> parameters) {
        try {            
            return defaultHTTP("/settings/user/list", parameters).get();
        } catch (IOException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult findUser(Map<String, Object> parameters) {
        try {            
            return defaultHTTP("/settings/user/find", parameters).get();
        } catch (IOException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult addUser(Map<String, Object> parameters) {
        try {            
            return defaultHTTP("/settings/user/add", parameters).post();
        } catch (IOException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult editUser(Map<String, Object> parameters) {
        try {            
            return defaultHTTP("/settings/user/edit", parameters).post();
        } catch (IOException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult removeUser(Map<String, Object> parameters) {
        try {            
            return defaultHTTP("/settings/user/remove", parameters).post();
        } catch (IOException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult changePassword(Map<String, Object> parameters) {
        try {            
            return defaultHTTP("/user/change-password", parameters).post();
        } catch (IOException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult listUserGroup(Map<String, Object> parameters) {
        try {            
            return defaultHTTP("/settings/user-group/list", parameters).get();
        } catch (IOException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult findUserGroup(Map<String, Object> parameters) {
        try {            
            return defaultHTTP("/settings/user-group/find", parameters).get();
        } catch (IOException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult addUserGroup(Map<String, Object> parameters) {
        try {            
            return defaultHTTP("/settings/user-group/add", parameters).post();
        } catch (IOException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult editUserGroup(Map<String, Object> parameters) {
        try {            
            return defaultHTTP("/settings/user-group/edit", parameters).post();
        } catch (IOException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult removeUserGroup(Map<String, Object> parameters) {
        try {            
            return defaultHTTP("/settings/user-group/remove", parameters).post();
        } catch (IOException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult addEmailQueue(Map<String, Object> parameters) {
        try {            
//            HTTP http = new HTTP();
//            http.setUrl(hostUrl + "/email-queue/add");
//            http.setBody(parameters);
            return defaultHTTP("/email-queue/add", parameters).post();
        } catch (IOException e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

}
