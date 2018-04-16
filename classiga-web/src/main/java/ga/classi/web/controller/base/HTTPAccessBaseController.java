package ga.classi.web.controller.base;

import java.util.Map;

import org.json.simple.JSONArray;

import ga.classi.commons.helper.ActionResult;
import ga.classi.commons.helper.CommonConstants;
import ga.classi.commons.helper.CommonUtils;
import ga.classi.commons.web.helper.HTTP;
import ga.classi.commons.web.helper.HTTPResponse;
import ga.classi.web.helper.SessionKeyConstants;
import ga.classi.web.helper.SessionManager;
import lombok.extern.slf4j.Slf4j;

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
    protected HTTP getPredefinedHttpClient() {
        HTTP http = new HTTP(hostUrl);
        http.setHeader("Accept-Language", getSystem(CommonConstants.SYSTEM_KEY_LANGUAGE_CODE));
        return http;
    }

    /**
     * Returns {@link HTTP} with host defined in project properties file for specified path.
     * @param path The path of the API to access.
     * @return The HttpClient with predefined configuration.
     */
    protected HTTP getPredefinedHttpClient(String path) {
        HTTP httpClient = getPredefinedHttpClient();
        httpClient.setPath(path);
        return httpClient;
    }
    
    /**
     * Returns {@link HTTP} with host defined in project properties file for specified path and parameters.
     * @param path The path of the API to access.
     * @param parameters The parameters for the API.
     * @return The HttpClient with predefined configuration.
     */
    protected HTTP getPredefinedHttpClient(String path, Map<String, Object> parameters) {
        HTTP httpClient = getPredefinedHttpClient(path);
        httpClient.setParameters(parameters);
        return httpClient;
    }
    
    @Override
    public void loadSystems() {
        log.debug("Load systems to session ...");
        try {
            // Cannot use method getDefinedHttpClient() because it will add a header 
            // which will call this method. It will cause a forever loop. 
            // Just use a new HttpClient().
            HTTPResponse response = new HTTP(hostUrl, "/settings/system/list").get();
            if (response != null) {
                if (CommonConstants.SUCCESS.equals(response.getStatus())) {
                    SessionManager.set(SessionKeyConstants.SYSTEMS, (JSONArray) response.getContent());
                    updateLocale(getSystem(CommonConstants.SYSTEM_KEY_LANGUAGE_CODE));
                } else {    
                    log.error(response.getMessage());
                }
            }
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e));
        }
    }

    @Override
    public ActionResult editSystems(Map<String, Object> parameters, String languageCode) {
        try {            
            HTTP httpClient = getPredefinedHttpClient("/settings/system/edit", parameters);
            httpClient.setHeader("Accept-Language", languageCode);
            return httpClient.post();
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult login(Map<String, Object> parameters) {   
        try {            
            return getPredefinedHttpClient("/login", parameters).post();
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult listUser(Map<String, Object> parameters) {
        try {            
            return getPredefinedHttpClient("/settings/user/list", parameters).get();
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult findUser(Map<String, Object> parameters) {
        try {            
            return getPredefinedHttpClient("/settings/user/find", parameters).get();
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult addUser(Map<String, Object> parameters) {
        try {            
            return getPredefinedHttpClient("/settings/user/add", parameters).post();
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult editUser(Map<String, Object> parameters) {
        try {            
            return getPredefinedHttpClient("/settings/user/edit", parameters).post();
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult removeUser(Map<String, Object> parameters) {
        try {            
            return getPredefinedHttpClient("/settings/user/remove", parameters).post();
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult listUserGroup(Map<String, Object> parameters) {
        try {            
            return getPredefinedHttpClient("/settings/user-group/list", parameters).get();
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult findUserGroup(Map<String, Object> parameters) {
        try {            
            return getPredefinedHttpClient("/settings/user-group/find", parameters).get();
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult addUserGroup(Map<String, Object> parameters) {
        try {            
            return getPredefinedHttpClient("/settings/user-group/add", parameters).post();
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult editUserGroup(Map<String, Object> parameters) {
        try {            
            return getPredefinedHttpClient("/settings/user-group/edit", parameters).post();
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

    @Override
    public ActionResult removeUserGroup(Map<String, Object> parameters) {
        try {            
            return getPredefinedHttpClient("/settings/user-group/remove", parameters).post();
        } catch (Exception e) {
            log.error(CommonUtils.getExceptionMessage(e), e);
            return errorActionResult();
        }
    }

}
