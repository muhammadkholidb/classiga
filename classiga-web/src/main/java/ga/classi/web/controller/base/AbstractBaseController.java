package ga.classi.web.controller.base;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import ga.classi.commons.helper.ActionResult;
import ga.classi.commons.helper.CommonConstants;
import ga.classi.commons.helper.DefaultUser;
import ga.classi.commons.helper.MessageHelper;
import ga.classi.commons.helper.StringConstants;
import ga.classi.web.helper.JSONHelper;
import ga.classi.web.helper.MenuKeyConstants;
import ga.classi.web.helper.MenuLoader;
import ga.classi.web.helper.ModelKeyConstants;
import ga.classi.web.helper.SessionKeyConstants;
import ga.classi.web.helper.SessionManager;
import ga.classi.web.helper.UIHelper;
import ga.classi.web.ui.Notify;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractBaseController implements IBaseController {

    @Autowired
    protected HttpServletRequest httpServletRequest;

    @Autowired
    protected HttpServletResponse httpServletResponse;

    @Autowired
    protected ServletContext servletContext;
    
    @Autowired
    protected MessageSource messageSource;
    
    @Autowired
    protected MessageHelper messageHelper;

    @Autowired
    protected LocaleResolver localeResolver;

    @Autowired
    @Qualifier("applicationProp")
    protected Properties applicationProperties;

    protected String applicationName;
    protected String applicationVersion;
    protected boolean showApplicationInfo;
    protected String defaultRedirect;
    
    protected static final String DEFAULT_TEMPLATE_CODE = "vali";
    protected static final String DEFAULT_LANGUAGE_CODE = "in";
    
    // Read http://docs.oracle.com/javaee/7/api/javax/annotation/PostConstruct.html
    @PostConstruct
    void initialize() {
        log.debug("Initialize {} ...", getClass()); 
        applicationName = applicationProperties.getProperty("application.info.name");
        applicationVersion = applicationProperties.getProperty("application.info.version");
        showApplicationInfo = Boolean.valueOf(applicationProperties.getProperty("application.info.showinpagetitle"));
        defaultRedirect = applicationProperties.getProperty("login.redirect.path.default");
        postConstruct();
    }

    protected abstract void postConstruct();
 
    @Override
    public ModelAndView view(ModelAndView mav) {
        if (userHasLoggedIn()) {            
            mav.addObject(ModelKeyConstants.USER, SessionManager.get(SessionKeyConstants.USER));
            mav.addObject(ModelKeyConstants.USER_GROUP, SessionManager.get(SessionKeyConstants.USER_GROUP));
            mav.addObject(ModelKeyConstants.MENUS, getAllowedMenus());
            JSONObject currentMenu = getCurrentMenu();
            if (currentMenu != null) {                
                mav.addObject(ModelKeyConstants.CURRENT_MENU, currentMenu);
                mav.addObject(ModelKeyConstants.CAN_MODIFY, isMenuModifyAllowed((String) currentMenu.get(MenuKeyConstants.CODE)));
            }
        }
        mav.addObject(ModelKeyConstants.APP_NAME, applicationName);
        mav.addObject(ModelKeyConstants.APP_VERSION, applicationVersion);
        mav.addObject(ModelKeyConstants.SHOW_APP_INFO, showApplicationInfo);
        
        String templateCode = getSystem(CommonConstants.SYSTEM_KEY_TEMPLATE_CODE);
        String languageCode = getSystem(CommonConstants.SYSTEM_KEY_LANGUAGE_CODE);
        
        mav.addObject(ModelKeyConstants.LANGUAGE_CODE, languageCode == null ? DEFAULT_LANGUAGE_CODE : languageCode);
        mav.addObject(ModelKeyConstants.TEMPLATE_CODE, templateCode == null ? DEFAULT_TEMPLATE_CODE : templateCode);

        mav.setViewName((templateCode == null ? DEFAULT_TEMPLATE_CODE : templateCode) + "/" + mav.getViewName());
        
        loadFlashToView(mav);
        
        return mav;
    }

    @Override
    public ModelAndView view(String view, Map<String, Object> model) {
        ModelAndView mav = new ModelAndView(view);
        mav.addAllObjects(model);
        return view(mav);
    }

    @Override
    public ModelAndView view(String view) {
        return view(view, null);
    }

    @Override
    public ModelAndView viewAndNotify(ModelAndView mav, String message, String notificationType) {
        ModelAndView mav1 = view(mav);
        mav1.addObject(ModelKeyConstants.NOTIFY, UIHelper.createNotification(StringConstants.EMPTY, message, notificationType));
        return mav1;
    }

    @Override
    public ModelAndView viewAndNotify(String view, Map<String, Object> model, String message, String notificationType) {
        ModelAndView mav = new ModelAndView(view);
        mav.addAllObjects(model);
        return viewAndNotify(mav, message, notificationType);
    }

    @Override
    public ModelAndView viewAndNotify(String view, String message, String notificationType) {
        return viewAndNotify(view, null, message, notificationType);
    }

    @Override
    public ModelAndView viewAndNotifyError(ModelAndView mav, String message) {
        return viewAndNotify(mav, message, Notify.DANGER);
    }

    @Override
    public ModelAndView viewAndNotifyError(String view, Map<String, Object> model, String message) {
        return viewAndNotify(view, model, message, Notify.DANGER);
    }

    @Override
    public ModelAndView viewAndNotifyError(String view, String message) {
        return viewAndNotify(view, message, Notify.DANGER);
    }

    @Override
    public ModelAndView viewAndNotifyInfo(ModelAndView mav, String message) {
        return viewAndNotify(mav, message, Notify.INFO);
    }

    @Override
    public ModelAndView viewAndNotifyInfo(String view, Map<String, Object> model, String message) {
        return viewAndNotify(view, model, message, Notify.INFO);
    }

    @Override
    public ModelAndView viewAndNotifyInfo(String view, String message) {
        return viewAndNotify(view, message, Notify.INFO);
    }

    @Override
    public ModelAndView viewAndNotifyWarning(ModelAndView mav, String message) {
        return viewAndNotify(mav, message, Notify.WARNING);
    }

    @Override
    public ModelAndView viewAndNotifyWarning(String view, Map<String, Object> model, String message) {
        return viewAndNotify(view, model, message, Notify.WARNING);
    }

    @Override
    public ModelAndView viewAndNotifyWarning(String view, String message) {
        return viewAndNotify(view, message, Notify.WARNING);
    }

    @Override
    public ModelAndView viewAndNotifySuccess(ModelAndView mav, String message) {
        return viewAndNotify(mav, message, Notify.SUCCESS);
    }

    @Override
    public ModelAndView viewAndNotifySuccess(String view, Map<String, Object> model, String message) {
        return viewAndNotify(view, model, message, Notify.SUCCESS);
    }

    @Override
    public ModelAndView viewAndNotifySuccess(String view, String message) {
        return viewAndNotify(view, message, Notify.SUCCESS);
    }

    @Override
    public ModelAndView redirect(String path) {
        return redirect(path, null);
    }

    @Override
    public ModelAndView redirect(String path, HashMap<String, Object> flashModel) {
        SessionManager.set(SessionKeyConstants.FLASH, flashModel);
        return new ModelAndView("redirect:" + path);
    }

    @Override
    public ModelAndView redirectAndNotify(String path, String message, String notificationType) {
        HashMap<String, Object> flashModel = new HashMap<String, Object>();
        flashModel.put(ModelKeyConstants.NOTIFY, UIHelper.createNotification(StringConstants.EMPTY, message, notificationType));
        return redirect(path, flashModel);
    }

    @Override
    public ModelAndView redirectAndNotify(String path, HashMap<String, Object> flashModel, String message, String notificationType) {
        if (flashModel != null) {
            flashModel.put(ModelKeyConstants.NOTIFY, UIHelper.createNotification(StringConstants.EMPTY, message, notificationType));
            return redirect(path, flashModel);
        }
        return redirectAndNotify(path, message, notificationType);
    }

    @Override
    public ModelAndView redirectAndNotifyError(String path, String message) {
        return redirectAndNotify(path, message, Notify.DANGER);
    }

    @Override
    public ModelAndView redirectAndNotifyError(String path, HashMap<String, Object> flashModel, String message) {
        return redirectAndNotify(path, flashModel, message, Notify.DANGER);
    }

    @Override
    public ModelAndView redirectAndNotifyInfo(String path, String message) {
        return redirectAndNotify(path, message, Notify.INFO);
    }

    @Override
    public ModelAndView redirectAndNotifyInfo(String path, HashMap<String, Object> flashModel, String message) {
        return redirectAndNotify(path, flashModel, message, Notify.INFO);
    }

    @Override
    public ModelAndView redirectAndNotifySuccess(String path, String message) {
        return redirectAndNotify(path, message, Notify.SUCCESS);
    }

    @Override
    public ModelAndView redirectAndNotifySuccess(String path, HashMap<String, Object> flashModel, String message) {
        return redirectAndNotify(path, flashModel, message, Notify.SUCCESS);
    }

    @Override
    public ModelAndView redirectAndNotifyWarning(String path, String message) {
        return redirectAndNotify(path, message, Notify.WARNING);
    }

    @Override
    public ModelAndView redirectAndNotifyWarning(String path, HashMap<String, Object> flashModel, String message) {
        return redirectAndNotify(path, flashModel, message, Notify.WARNING);
    }

    private void loadFlashToView(ModelAndView mav) {
        Map<String, Object> flashModel = SessionManager.get(SessionKeyConstants.FLASH);
        if ((flashModel != null) && (mav != null)) {
            mav.addAllObjects(flashModel);
            SessionManager.remove(SessionKeyConstants.FLASH);
        }
    }

    private void loadMenus() {

        String dir = servletContext.getRealPath("/WEB-INF/");
        log.debug("Read menu files in {}", dir);

        MenuLoader menuLoader = new MenuLoader();
        menuLoader.loadFrom(dir);

        log.debug("Set menus to session ...");

        SessionManager.set(SessionKeyConstants.MENUS, menuLoader.getNestedMenus());
        SessionManager.set(SessionKeyConstants.FLAT_MENUS, menuLoader.getFlatMenus());
    }

    /**
     * Returns menus in nested or flat format.
     *
     * @param flat Set true to return menus in flat format.
     * @return The menus in nested or flat format.
     */
    @Override
    public JSONArray getMenus(boolean flat) {
        String key = flat ? SessionKeyConstants.FLAT_MENUS : SessionKeyConstants.MENUS;
        JSONArray menus = SessionManager.get(key);
        if (menus == null) {
            loadMenus();
            return SessionManager.get(key);
        }
        return menus;
    }

    /**
     * Returns menus in nested format.
     *
     * @return The menus in nested format.
     */
    @Override
    public JSONArray getMenus() {
        return getMenus(false);
    }

    /**
     * Returns allowed menus for currently logged in user.
     * @return The menus for currently logged in user.
     */
    @Override
    public JSONArray getAllowedMenus() { 
        JSONArray allowedMenus = SessionManager.get(SessionKeyConstants.ALLOWED_MENUS);
        if (allowedMenus == null) {
            loadAllowedMenus();
            return SessionManager.get(SessionKeyConstants.ALLOWED_MENUS);
        }
        return allowedMenus;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void loadAllowedMenus() {
        JSONArray menus = getMenus();
        JSONArray allowedMenus = new JSONArray();
        for (Object o1 : menus) {
            JSONObject menu = (JSONObject) o1;
            // This is parent menu, if the parent menu is not allowed to be viewed then the all submenus cannot be viewed as well
            if (!isMenuViewAllowed((String) menu.get(MenuKeyConstants.CODE))) {
                continue;
            }
            // Check allowed submenus
            JSONArray allowedSubmenus = new JSONArray();
            JSONArray submenus = (JSONArray) menu.get(MenuKeyConstants.SUBMENUS);
            if (submenus != null && !submenus.isEmpty()) {
                for (Object o2 : submenus) {
                    JSONObject sub = (JSONObject) o2;
                    if (isMenuViewAllowed((String) sub.get(MenuKeyConstants.CODE))) {
                        allowedSubmenus.add(sub);
                    }
                }
            }
            // Remove the original submenus from allowed menu
            JSONObject allowedMenu = JSONHelper.remove(menu, MenuKeyConstants.SUBMENUS);
            if (!allowedSubmenus.isEmpty()) {
                // Set new allowed submenus
                allowedMenu.put(MenuKeyConstants.SUBMENUS, allowedSubmenus);
            }
            allowedMenus.add(allowedMenu);
        }
        SessionManager.set(SessionKeyConstants.ALLOWED_MENUS, allowedMenus);
    }
    
    @Override
    public boolean isMenuAllowed(String can, String code) {
        JSONObject userGroup = SessionManager.get(SessionKeyConstants.USER_GROUP);
        if (DefaultUser.USER_GROUP_ID.equals(userGroup.get("id"))) {
            return true;
        }
        JSONArray menuPermissions = SessionManager.get(SessionKeyConstants.MENU_PERMISSIONS);
        log.debug("Menu permissions: {}", menuPermissions);
        for (Object o : menuPermissions) {
            JSONObject menuPermission = (JSONObject) o;
            if (menuPermission.get("menuCode").equals(code) && CommonConstants.YES.equals(menuPermission.get(can))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isMenuViewAllowed(String code) {
        return isMenuAllowed("canView", code);
    }
    
    @Override
    public boolean isMenuModifyAllowed(String code) {
        return isMenuAllowed("canModify", code);
    }

    private static final List<String> ENDING_PATHS = Arrays.asList("/add", "/edit", "/remove", "/list");
    
    private JSONObject getCurrentMenu() {
        String contextPath = httpServletRequest.getContextPath();
        String requestUrl = httpServletRequest.getRequestURI(); // This method returns the path before URL query strings
        if (requestUrl.endsWith("/")) {
            requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/'));  // Remove last character "/"
        }
        for (String path : ENDING_PATHS) {
            if (requestUrl.contains(path)) {
                requestUrl = requestUrl.substring(0, requestUrl.indexOf(path));
                break;
            }
        }
        for (Object o : getMenus(true)) {
            JSONObject menu = (JSONObject) o; 
            String path = (String) menu.get(MenuKeyConstants.PATH);
            if (path != null && requestUrl.substring(contextPath.length(), requestUrl.length()).equals(path)) {
                return menu;
            }
        }
        return null;
    }
    
    /**
     * Returns supported languages based on properties files in application.
     * @param key key in .properties files to check.
     * @return List of supported locales.
     */
    // Modification from https://stackoverflow.com/questions/5380534/dynamically-generate-a-list-of-available-languages-in-spring-mvc
    @Override
    public List<Locale> getSupportedLocales(String key) {
        log.info("Get supported locales ...");
        ArrayList<Locale> supportedLocales = SessionManager.get(SessionKeyConstants.SUPPORTED_LOCALES);
        if (supportedLocales != null && !supportedLocales.isEmpty()) {
            return supportedLocales;
        }
        if (key == null || key.isEmpty()) {
            return new ArrayList<Locale>();
        }
        Locale[] availableLocales = Locale.getAvailableLocales();
        Arrays.sort(availableLocales, new Comparator<Locale>() {

            @Override
            public int compare(Locale locale1, Locale locale2) {
                return locale1.toString().compareTo(locale2.toString());
            }
        });
        Map<String, Locale> mapSupportedLocales = new HashMap<String, Locale>();
        for (Locale locale : availableLocales) {
            String languageCode = locale.getLanguage();
            String message = messageSource.getMessage(key, null, null, locale);
            if (message != null && !mapSupportedLocales.containsKey(languageCode)) {
                mapSupportedLocales.put(languageCode, locale);
            }
        }
        supportedLocales = new ArrayList<Locale>();
        for (Locale locale : mapSupportedLocales.values()) {
            supportedLocales.add(locale);
        }
        log.info("{} supported locales found: {}", supportedLocales.size(), supportedLocales);
        SessionManager.set(SessionKeyConstants.SUPPORTED_LOCALES, supportedLocales);
        return supportedLocales;
    }

    /**
     * Returns available templates in the application.
     * @return List of available templates.
     */
    @Override
    public List<String> getAvailableTemplates() {
        log.info("Get available templates ...");
        ArrayList<String> templates = SessionManager.get(SessionKeyConstants.AVAILABLE_TEMPLATES);
        if (templates != null && !templates.isEmpty()) {
            return templates;
        }
        templates = new ArrayList<String>();
        File[] files = new File(servletContext.getRealPath("/WEB-INF/templates/")).listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                templates.add(file.getName());
            }
        }
        log.info("{} available templates found: {}", templates.size(), templates);
        SessionManager.set(SessionKeyConstants.AVAILABLE_TEMPLATES, templates);
        return templates;
    }
    
    @Override
    public boolean isGet() {
        return "GET".equals(httpServletRequest.getMethod());
    }

    @Override
    public boolean isPost() {
        return "POST".equals(httpServletRequest.getMethod());
    }

    @Override
    public void updateLocale(String languageCode) {
        updateLocale(StringUtils.parseLocaleString(languageCode));
    }

    @Override
    public void updateLocale(Locale newLocale) {
        localeResolver.setLocale(httpServletRequest, httpServletResponse, newLocale);
        messageHelper.setDefaultLocale(newLocale);
    }

    @Override
    public String getFullRequestURL() {
        return httpServletRequest.getRequestURL() + (httpServletRequest.getQueryString() != null ? "?" + httpServletRequest.getQueryString() : "");
    }

    // Implements IBaseControllerSystem
    
    @Override
    public JSONArray getSystems() {
        log.debug("Get systems ...");
        JSONArray systems = SessionManager.get(SessionKeyConstants.SYSTEMS);
        if (systems == null) {
            loadSystems();
            return SessionManager.get(SessionKeyConstants.SYSTEMS);
        }
        return systems;
    }

    @Override
    public String getSystem(String key) {
        log.debug("Get system: {}", key);
        JSONArray systems = getSystems();
        if (systems != null) {
            for (Object object : systems) {
                JSONObject json = (JSONObject) object;
                String dataKey = (String) json.get("dataKey");
                String dataValue = (String) json.get("dataValue");
                if (dataKey.equals(key)) {
                    return dataValue;
                }
            }
        }
        log.debug("Key '{}' not found.", key);
        return null;
    }

    // Implements IBaseControllerUser
    
    @Override
    public boolean userHasLoggedIn() {
        return SessionManager.get(SessionKeyConstants.USER) != null;
    }

    @Override
    public String getApplicationName() {
        return applicationName;
    }

    @Override
    public String getApplicationVersion() {
        return applicationVersion;
    }

    @Override
    public boolean isShowApplicationInfo() {
        return showApplicationInfo;
    }

    @Override
    public String getDefaultRedirect() {
        return defaultRedirect;
    }

    @Override
    public String getDefaultTemplateCode() {
        return DEFAULT_TEMPLATE_CODE;
    }

    @Override
    public String getDefaultLanguageCode() {
        return DEFAULT_LANGUAGE_CODE;
    }

    @Override
    public String createMessage(String code, Object[] arguments) {
        Locale locale = localeResolver.resolveLocale(httpServletRequest);
        if (locale != null) {
            return messageHelper.getMessage(code, arguments, locale);
        }
        return messageHelper.getMessage(code, arguments);
    }

    @Override
    public String createMessage(String code) {
        return createMessage(code, null);
    }

    @Override
    public ActionResult createActionResult(String status, String message, JSONObject data) {
        ActionResult result = new ActionResult();
        result.setStatus(status);
        result.setMessage(message);
        result.setData(data);
        return result.parseData();
    }

    @Override
    public ActionResult successActionResult(String message, JSONObject data) {
        return createActionResult(CommonConstants.SUCCESS, message, data);
    }

    @Override
    public ActionResult successActionResult(JSONObject data) {
        return successActionResult(StringConstants.EMPTY, data);
    }

    @Override
    public ActionResult failActionResult(String message) {
        return createActionResult(CommonConstants.FAIL, message, new JSONObject());
    }

    @Override
    public ActionResult errorActionResult() {
        return createActionResult(CommonConstants.ERROR, createMessage("error.ServerError"), new JSONObject());
    }
        
}
