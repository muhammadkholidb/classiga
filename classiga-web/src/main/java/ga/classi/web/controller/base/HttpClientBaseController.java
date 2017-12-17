package ga.classi.web.controller.base;

import java.io.IOException;
import java.util.Properties;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import ga.classi.commons.helper.ActionResult;
import ga.classi.commons.helper.CommonConstants;
import ga.classi.commons.helper.CommonUtils;
import ga.classi.commons.helper.HttpClient;
import ga.classi.commons.helper.HttpClientResponse;
import ga.classi.web.helper.SessionKeyConstants;
import ga.classi.web.helper.SessionManager;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author eatonmunoz
 */
@Slf4j
public class HttpClientBaseController extends AbstractBaseController implements IBaseController {

    @Autowired
    @Qualifier("applicationProp")
    protected Properties applicationProperties;

    protected String hostUrl;

    @Override
    protected void postConstruct() {
        hostUrl = applicationProperties.getProperty("rest.url.host");
    }

    /**
     * Returns {@link HttpClient} with host defined in project properties file.
     * @return
     */
    protected HttpClient getPredefinedHttpClient() {
        HttpClient httpClient = new HttpClient(hostUrl);
        httpClient.setHeader("Accept-Language", getSystem(CommonConstants.SYSTEM_KEY_LANGUAGE_CODE));
        return httpClient;
    }

    /**
     * Returns {@link HttpClient} with host defined in project properties file for specified path.
     * @param path
     * @return
     */
    protected HttpClient getPredefinedHttpClient(String path) {
        HttpClient httpClient = getPredefinedHttpClient();
        httpClient.setPath(path);
        return httpClient;
    }
    
    /**
     * Returns {@link HttpClient} with host defined in project properties file for specified path and parameters.
     * @param path
     * @param parameters
     * @return
     */
    protected HttpClient getPredefinedHttpClient(String path, JSONObject parameters) {
        HttpClient httpClient = getPredefinedHttpClient(path);
        httpClient.setParameters(parameters);
        return httpClient;
    }
    
    public void loadSystems() {
        log.debug("Load systems to session ...");
        try {
            // Cannot use method getDefinedHttpClient() because it will add a header 
            // which will call this method. It will cause a forever loop. 
            // Just use a new HttpClient().
            HttpClientResponse response = new HttpClient(hostUrl, "/settings/system/list").get();
            if (response != null) {
                if (CommonConstants.SUCCESS.equals(response.getStatus())) {
                    SessionManager.set(SessionKeyConstants.SYSTEMS, response.getContent());
                    updateLocale(getSystem(CommonConstants.SYSTEM_KEY_LANGUAGE_CODE));
                } else {    
                    log.error(response.getMessage());
                }
            }
        } catch (IOException e) {
            log.error(CommonUtils.getExceptionMessage(e));
        }
    }

    public ActionResult editSystems(JSONObject parameters, String languageCode) throws IOException {
        HttpClient httpClient = getPredefinedHttpClient("/settings/system/edit", parameters);
        httpClient.setHeader("Accept-Language", languageCode);
        return httpClient.post();
    }

    public ActionResult login(JSONObject parameters) throws IOException {   
        return getPredefinedHttpClient("/login", parameters).post();
    }

    public ActionResult listUser(JSONObject parameters) throws IOException {
        return getPredefinedHttpClient("/settings/user/list", parameters).get();
    }

    public ActionResult findUser(JSONObject parameters) throws IOException {
        return getPredefinedHttpClient("/settings/user/find", parameters).get();
    }

    public ActionResult addUser(JSONObject parameters) throws IOException {
        return getPredefinedHttpClient("/settings/user/add", parameters).post();
    }

    public ActionResult editUser(JSONObject parameters) throws IOException {
        return getPredefinedHttpClient("/settings/user/edit", parameters).post();
    }

    public ActionResult removeUser(JSONObject parameters) throws IOException {
        return getPredefinedHttpClient("/settings/user/remove", parameters).post();
    }

    public ActionResult listUserGroup(JSONObject parameters) throws IOException {
        return getPredefinedHttpClient("/settings/user-group/list", parameters).get();
    }

    public ActionResult findUserGroup(JSONObject parameters) throws IOException {
        return getPredefinedHttpClient("/settings/user-group/find", parameters).get();
    }

    public ActionResult addUserGroup(JSONObject parameters) throws IOException {
        return getPredefinedHttpClient("/settings/user-group/add", parameters).post();
    }

    public ActionResult editUserGroup(JSONObject parameters) throws IOException {
        return getPredefinedHttpClient("/settings/user-group/edit", parameters).post();
    }

    public ActionResult removeUserGroup(JSONObject parameters) throws IOException {
        return getPredefinedHttpClient("/settings/user-group/remove", parameters).post();
    }

}
