package ga.classi.web.controller;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import ga.classi.commons.helper.CommonConstants;
import ga.classi.commons.helper.CommonUtils;
import ga.classi.commons.helper.HttpClient;
import ga.classi.commons.helper.HttpClientResponse;
import ga.classi.web.helper.ModelKeyConstants;
import ga.classi.web.helper.SessionKeyConstants;
import ga.classi.web.helper.SessionManager;

/**
 * 
 * @author eatonmunoz
 */
public abstract class HttpClientBaseController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(HttpClientBaseController.class);

    protected String hostUrl;

    @Override
    protected void postConstruct() {
        hostUrl = applicationProp.getProperty("rest.url.host");
    }

    /**
     * Returns {@link HttpClient} with host defined in project properties file.
     * @return
     */
    protected HttpClient getDefinedHttpClient() {
        HttpClient httpClient = new HttpClient(hostUrl);
        httpClient.setHeader("Accept-Language", getSystem(CommonConstants.SYSTEM_KEY_LANGUAGE_CODE));
        return httpClient;
    }

    /**
     * Returns {@link HttpClient} with host defined in project properties file for specified path.
     * @param path
     * @return
     */
    protected HttpClient getDefinedHttpClient(String path) {
        HttpClient httpClient = getDefinedHttpClient();
        httpClient.setPath(path);
        return httpClient;
    }
    
    /**
     * Returns {@link HttpClient} with host defined in project properties file for specified path and parameters.
     * @param path
     * @param parameters
     * @return
     */
    protected HttpClient getDefinedHttpClient(String path, JSONObject parameters) {
        HttpClient httpClient = getDefinedHttpClient(path);
        httpClient.setParameters(parameters);
        return httpClient;
    }
    
    protected JSONArray getSystems() {
        log.debug("Get systems ...");
        JSONArray systems = SessionManager.get(SessionKeyConstants.SYSTEMS);
        if (systems == null) {
            loadSystems();
            return SessionManager.get(SessionKeyConstants.SYSTEMS);
        }
        return systems;
    }

    protected String getSystem(String key) {
        log.debug("Get system: {}", key);
        JSONArray systems = getSystems();
        for (Object object : systems) {
            JSONObject json = (JSONObject) object;
            String dataKey = (String) json.get("dataKey");
            String dataValue = (String) json.get("dataValue");
            if (dataKey.equals(key)) {
                return dataValue;
            }
        }
        log.debug("Key '{}' not found.", key);
        return null;
    }

    protected void loadSystems() {
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

    @Override
    protected ModelAndView view(ModelAndView mav) {
        String templateCode = getSystem(CommonConstants.SYSTEM_KEY_TEMPLATE_CODE);
        String languageCode = getSystem(CommonConstants.SYSTEM_KEY_LANGUAGE_CODE);
        ModelAndView parentMav = super.view(mav);
        parentMav.setViewName(templateCode + "/" + mav.getViewName());
        parentMav.addObject(ModelKeyConstants.LANGUAGE_CODE, languageCode);
        parentMav.addObject(ModelKeyConstants.TEMPLATE_CODE, templateCode);
        return parentMav; 
    }

}
