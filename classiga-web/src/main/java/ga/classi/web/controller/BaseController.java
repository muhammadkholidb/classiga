package ga.classi.web.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

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
import javax.annotation.PostConstruct;

/**
 *
 * @author eatonmunoz
 */
public abstract class BaseController {

    private static final Logger log = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    protected HttpServletRequest httpServletRequest;

    @Autowired
    protected HttpServletResponse httpServletResponse;

    @Autowired
    protected LocaleResolver localeResolver;

    @Autowired
    protected ServletContext servletContext;

    @Autowired
    protected WebApplicationContext webApplicationContext;
    
    @Autowired
    protected MessageSource messageSource;
    
    @Autowired
    protected MessageHelper messageHelper;

    @Autowired
    @Qualifier("applicationProp")
    protected Properties applicationProp;

    @Autowired
    @Qualifier("usersProp")
    protected Properties usersProp;

    protected String applicationName;
    protected String applicationVersion;
    protected boolean showApplicationInfo;
    protected String defaultRedirect;
    
    // Read http://docs.oracle.com/javaee/7/api/javax/annotation/PostConstruct.html
    @PostConstruct
    void initialize() {
        log.debug("Initialize {} ...", getClass()); 
        applicationName = applicationProp.getProperty("application.info.name");
        applicationVersion = applicationProp.getProperty("application.info.version");
        showApplicationInfo = Boolean.valueOf(applicationProp.getProperty("application.info.showinpagetitle"));
        defaultRedirect = applicationProp.getProperty("login.redirect.path.default");
        postConstruct();
    }

    protected abstract void postConstruct();

    protected String getFullRequestUrl() {
        return httpServletRequest.getRequestURL() + (httpServletRequest.getQueryString() != null ? "?" + httpServletRequest.getQueryString() : "");
    }
 
    /**
     * Returns menus in nested or flat format.
     *
     * @param flat
     * @return
     */
    protected JSONArray getMenus(boolean flat) {
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
     * @return
     */
    protected JSONArray getMenus() {
        return getMenus(false);
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
     * 
     * @return
     */
    protected JSONArray getAllowedMenus() { 
        JSONArray allowedMenus = SessionManager.get(SessionKeyConstants.ALLOWED_MENUS);
        if (allowedMenus == null) {
            loadAllowedMenus();
            return SessionManager.get(SessionKeyConstants.ALLOWED_MENUS);
        }
        return allowedMenus;
    }
    
    /**
     * 
     */
    @SuppressWarnings("unchecked")
    protected void loadAllowedMenus() {
        JSONArray menus = getMenus();
        JSONObject userGroup = SessionManager.get(SessionKeyConstants.USER_GROUP);
        // Allow all menus if login using default user
        if (DefaultUser.USER_GROUP_ID.equals(userGroup.get("id"))) {
            SessionManager.set(SessionKeyConstants.ALLOWED_MENUS, menus);
            return;
        }
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
            if (allowedSubmenus != null && !allowedSubmenus.isEmpty()) {
                // Set new allowed submenus
                allowedMenu.put(MenuKeyConstants.SUBMENUS, allowedSubmenus);
            }
            allowedMenus.add(allowedMenu);
        }
        SessionManager.set(SessionKeyConstants.ALLOWED_MENUS, allowedMenus);
    }
    
    private boolean isMenuAllowed(String can, String code) {
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

    private boolean isMenuViewAllowed(String code) {
        return isMenuAllowed("canView", code);
    }
    
    private boolean isMenuModifyAllowed(String code) {
        return isMenuAllowed("canModify", code);
    }
    
    /**
     * Returns supported languages based on properties files in application.
     * @param key key in .properties files to check.
     * @return
     */
    // Modification from https://stackoverflow.com/questions/5380534/dynamically-generate-a-list-of-available-languages-in-spring-mvc
    public List<Locale> getSupportedLocales(String key) {
        log.info("Get supported locales ...");
        List<Locale> supportedLocales = SessionManager.get(SessionKeyConstants.SUPPORTED_LOCALES);
        if (supportedLocales != null && !supportedLocales.isEmpty()) {
            return supportedLocales;
        }
        if (key == null || key.isEmpty()) {
            return null;
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
     * 
     * @return
     */
    protected List<String> getAvailableTemplates() {
        log.info("Get available templates ...");
        List<String> templates = SessionManager.get(SessionKeyConstants.AVAILABLE_TEMPLATES);
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
    
    protected boolean userHasLoggedIn() {
        return SessionManager.get(SessionKeyConstants.USER) != null;
    }
    
    protected boolean isGet() {
        return "GET".equals(httpServletRequest.getMethod());
    }

    protected boolean isPost() {
        return "POST".equals(httpServletRequest.getMethod());
    }

    protected void updateLocale(String languageCode) {
        updateLocale(StringUtils.parseLocaleString(languageCode));
    }

    protected void updateLocale(Locale newLocale) {
        localeResolver.setLocale(httpServletRequest, httpServletResponse, newLocale);
        messageHelper.setDefaultLocale(newLocale);
    }
    
    private final List<String> ENDING_PATHS = Arrays.asList("/add", "/edit", "/remove", "/list");
    
    private JSONObject getCurrentMenu() {
        String contextPath = httpServletRequest.getContextPath();
        String requestUrl = httpServletRequest.getRequestURI(); // This method returns the path before URL query strings
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
     * 
     * @param mav
     * @return
     */
    protected ModelAndView view(ModelAndView mav) {
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
        loadFlashToView(mav);
        return mav;
    }

    protected ModelAndView view(String view, Map<String, Object> model) {
        ModelAndView mav = new ModelAndView(view);
        mav.addAllObjects(model);
        return view(mav);
    }

    protected ModelAndView view(String view) {
        return view(view, null);
    }

    protected ModelAndView viewAndNotify(ModelAndView mav, String message, String notificationType) {
        ModelAndView mav1 = view(mav);
        mav1.addObject(ModelKeyConstants.NOTIFY, UIHelper.createNotification(StringConstants.EMPTY, message, notificationType));
        return mav1;
    }

    protected ModelAndView viewAndNotify(String view, Map<String, Object> model, String message, String notificationType) {
        ModelAndView mav = new ModelAndView(view);
        mav.addAllObjects(model);
        return viewAndNotify(mav, message, notificationType);
    }

    protected ModelAndView viewAndNotify(String view, String message, String notificationType) {
        return viewAndNotify(view, null, message, notificationType);
    }

    protected ModelAndView viewAndNotifyError(ModelAndView mav, String message) {
        return viewAndNotify(mav, message, Notify.DANGER);
    }

    protected ModelAndView viewAndNotifyError(String view, Map<String, Object> model, String message) {
        return viewAndNotify(view, model, message, Notify.DANGER);
    }

    protected ModelAndView viewAndNotifyError(String view, String message) {
        return viewAndNotify(view, message, Notify.DANGER);
    }

    protected ModelAndView viewAndNotifyInfo(ModelAndView mav, String message) {
        return viewAndNotify(mav, message, Notify.INFO);
    }

    protected ModelAndView viewAndNotifyInfo(String view, Map<String, Object> model, String message) {
        return viewAndNotify(view, model, message, Notify.INFO);
    }

    protected ModelAndView viewAndNotifyInfo(String view, String message) {
        return viewAndNotify(view, message, Notify.INFO);
    }

    protected ModelAndView viewAndNotifyWarning(ModelAndView mav, String message) {
        return viewAndNotify(mav, message, Notify.WARNING);
    }

    protected ModelAndView viewAndNotifyWarning(String view, Map<String, Object> model, String message) {
        return viewAndNotify(view, model, message, Notify.WARNING);
    }

    protected ModelAndView viewAndNotifyWarning(String view, String message) {
        return viewAndNotify(view, message, Notify.WARNING);
    }

    protected ModelAndView viewAndNotifySuccess(ModelAndView mav, String message) {
        return viewAndNotify(mav, message, Notify.SUCCESS);
    }

    protected ModelAndView viewAndNotifySuccess(String view, Map<String, Object> model, String message) {
        return viewAndNotify(view, model, message, Notify.SUCCESS);
    }

    protected ModelAndView viewAndNotifySuccess(String view, String message) {
        return viewAndNotify(view, message, Notify.SUCCESS);
    }

    protected ModelAndView redirect(String path) {
        return redirect(path, null);
    }

    protected ModelAndView redirect(String path, Map<String, Object> flashModel) {
        SessionManager.set(SessionKeyConstants.FLASH, flashModel);
        return new ModelAndView("redirect:" + path);
    }

    protected ModelAndView redirectAndNotify(String path, String message, String notificationType) {
        Map<String, Object> flashModel = new HashMap<String, Object>();
        flashModel.put(ModelKeyConstants.NOTIFY, UIHelper.createNotification(StringConstants.EMPTY, message, notificationType));
        return redirect(path, flashModel);
    }

    protected ModelAndView redirectAndNotify(String path, Map<String, Object> flashModel, String message, String notificationType) {
        if (flashModel != null) {
            flashModel.put(ModelKeyConstants.NOTIFY, UIHelper.createNotification(StringConstants.EMPTY, message, notificationType));
            return redirect(path, flashModel);
        }
        return redirectAndNotify(path, message, notificationType);
    }

    protected ModelAndView redirectAndNotifyError(String path, String message) {
        return redirectAndNotify(path, message, Notify.DANGER);
    }

    protected ModelAndView redirectAndNotifyError(String path, Map<String, Object> flashModel, String message) {
        return redirectAndNotify(path, flashModel, message, Notify.DANGER);
    }

    protected ModelAndView redirectAndNotifyInfo(String path, String message) {
        return redirectAndNotify(path, message, Notify.INFO);
    }

    protected ModelAndView redirectAndNotifyInfo(String path, Map<String, Object> flashModel, String message) {
        return redirectAndNotify(path, flashModel, message, Notify.INFO);
    }

    protected ModelAndView redirectAndNotifySuccess(String path, String message) {
        return redirectAndNotify(path, message, Notify.SUCCESS);
    }

    protected ModelAndView redirectAndNotifySuccess(String path, Map<String, Object> flashModel, String message) {
        return redirectAndNotify(path, flashModel, message, Notify.SUCCESS);
    }

    protected ModelAndView redirectAndNotifyWarning(String path, String message) {
        return redirectAndNotify(path, message, Notify.WARNING);
    }

    protected ModelAndView redirectAndNotifyWarning(String path, Map<String, Object> flashModel, String message) {
        return redirectAndNotify(path, flashModel, message, Notify.WARNING);
    }

    private void loadFlashToView(ModelAndView mav) {
        Map<String, Object> flashModel = SessionManager.get(SessionKeyConstants.FLASH);
        if ((flashModel != null) && (mav != null)) {
            mav.addAllObjects(flashModel);
            SessionManager.remove(SessionKeyConstants.FLASH);
        }
    }

}
