/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.web.controller.base;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import ga.classi.commons.utility.ActionResult;
import ga.classi.commons.utility.MessageHelper;

public abstract class BaseControllerAdapter {

    @Autowired
    protected IBaseController baseController;

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
    protected Properties applicationProperties;

    @Autowired
    @Qualifier("usersProp")
    protected Properties usersProperties;

    @Autowired
    protected ObjectMapper objectMapper;
    
    protected ModelAndView view(ModelAndView mav) {
        return baseController.view(mav);
    }

    protected ModelAndView view(String view, Map<String, Object> data) {
        return baseController.view(view, data);
    }

    protected ModelAndView view(String view) {
        return baseController.view(view);
    }

    protected ModelAndView viewAndNotify(ModelAndView mav, String message, String notificationType) {
        return baseController.viewAndNotify(mav, message, notificationType);
    }

    protected ModelAndView viewAndNotify(String view, Map<String, Object> data, String message, String notificationType) {
        return baseController.viewAndNotify(view, data, message, notificationType);
    }

    protected ModelAndView viewAndNotify(String view, String message, String notificationType) {
        return baseController.viewAndNotify(view, message, notificationType);
    }

    protected ModelAndView viewAndNotifyError(ModelAndView mav, String message) {
        return baseController.viewAndNotifyError(mav, message);
    }

    protected ModelAndView viewAndNotifyError(String view, Map<String, Object> data, String message) {
        return baseController.viewAndNotifyError(view, data, message);
    }

    protected ModelAndView viewAndNotifyError(String view, String message) {
        return baseController.viewAndNotifyError(view, message);
    }

    protected ModelAndView viewAndNotifyInfo(ModelAndView mav, String message) {
        return baseController.viewAndNotifyInfo(mav, message);
    }

    protected ModelAndView viewAndNotifyInfo(String view, Map<String, Object> data, String message) {
        return baseController.viewAndNotifyInfo(view, data, message);
    }

    protected ModelAndView viewAndNotifyInfo(String view, String message) {
        return baseController.viewAndNotifyInfo(view, message);
    }

    protected ModelAndView viewAndNotifyWarning(ModelAndView mav, String message) {
        return baseController.viewAndNotifyWarning(mav, message);
    }

    protected ModelAndView viewAndNotifyWarning(String view, Map<String, Object> data, String message) {
        return baseController.viewAndNotifyWarning(view, data, message);
    }

    protected ModelAndView viewAndNotifyWarning(String view, String message) {
        return baseController.viewAndNotifyWarning(view, message);
    }

    protected ModelAndView viewAndNotifySuccess(ModelAndView mav, String message) {
        return baseController.viewAndNotifySuccess(mav, message);
    }

    protected ModelAndView viewAndNotifySuccess(String view, Map<String, Object> data, String message) {
        return baseController.viewAndNotifySuccess(view, data, message);
    }

    protected ModelAndView viewAndNotifySuccess(String view, String message) {
        return baseController.viewAndNotifySuccess(view, message);
    }

    protected ModelAndView redirect(String path, RedirectAttributes ra) {
        return baseController.redirect(path, ra);
    }

    public ModelAndView redirectAndNotify(String path, RedirectAttributes ra, String message, String notificationType) {
        return baseController.redirectAndNotify(path, ra, message, notificationType);
    }

    public ModelAndView redirectAndNotifyError(String path, RedirectAttributes ra, String message) {
        return baseController.redirectAndNotifyError(path, ra, message);
    }

    public ModelAndView redirectAndNotifyInfo(String path, RedirectAttributes ra, String message) {
        return baseController.redirectAndNotifyInfo(path, ra, message);
    }

    public ModelAndView redirectAndNotifySuccess(String path, RedirectAttributes ra, String message) {
        return baseController.redirectAndNotifySuccess(path, ra, message);
    }

    public ModelAndView redirectAndNotifyWarning(String path, RedirectAttributes ra, String message) {
        return baseController.redirectAndNotifyWarning(path, ra, message);
    }
    
    protected ModelAndView redirect(String path) {
        return baseController.redirect(path);
    }

    protected ModelAndView redirect(String path, Map<String, Object> flashData) {
        return baseController.redirect(path, flashData);
    }

    protected ModelAndView redirectAndNotify(String path, String message, String notificationType) {
        return baseController.redirectAndNotify(path, message, notificationType);
    }

    protected ModelAndView redirectAndNotify(String path, Map<String, Object> flashData, String message, String notificationType) {
        return baseController.redirectAndNotify(path, flashData, message, notificationType);
    }

    protected ModelAndView redirectAndNotifyError(String path, String message) {
        return baseController.redirectAndNotifyError(path, message);
    }

    protected ModelAndView redirectAndNotifyError(String path, Map<String, Object> flashData, String message) {
        return baseController.redirectAndNotifyError(path, flashData, message);
    }

    protected ModelAndView redirectAndNotifyInfo(String path, String message) {
        return baseController.redirectAndNotifyInfo(path, message);
    }

    protected ModelAndView redirectAndNotifyInfo(String path, Map<String, Object> flashData, String message) {
        return baseController.redirectAndNotifyInfo(path, flashData, message);
    }

    protected ModelAndView redirectAndNotifySuccess(String path, String message) {
        return baseController.redirectAndNotifySuccess(path, message);
    }

    protected ModelAndView redirectAndNotifySuccess(String path, Map<String, Object> flashData, String message) {
        return baseController.redirectAndNotifySuccess(path, flashData, message);
    }

    protected ModelAndView redirectAndNotifyWarning(String path, String message) {
        return baseController.redirectAndNotifyWarning(path, message);
    }

    protected ModelAndView redirectAndNotifyWarning(String path, Map<String, Object> flashData, String message) {
        return baseController.redirectAndNotifyWarning(path, flashData, message);
    }

    protected Map<String, Object> getFlashData() {
        return baseController.getFlashData();
    }
    
    protected JSONArray getMenus(boolean flat) {
        return baseController.getMenus(flat);
    }

    protected JSONArray getMenus() {
        return baseController.getMenus();
    }

    protected JSONArray getAllowedMenus() { 
        return baseController.getAllowedMenus();
    }
    
    protected void loadAllowedMenus() {
        baseController.loadAllowedMenus();
    }
    
    protected boolean isMenuAllowed(String can, String code) {
        return baseController.isMenuAllowed(can, code);
    }

    protected boolean isMenuViewAllowed(String code) {
        return baseController.isMenuViewAllowed(code);
    }
    
    protected boolean isMenuModifyAllowed(String code) {
        return baseController.isMenuModifyAllowed(code);
    }

    protected List<Locale> getSupportedLocales(String key) {
        return baseController.getSupportedLocales(key);
    }

    protected List<String> getAvailableTemplates() {
        return baseController.getAvailableTemplates();
    }

    protected boolean isGet() {
        return baseController.isGet();
    }

    protected boolean isPost() {
        return baseController.isPost();
    }

    protected void updateLocale(String languageCode) {
        baseController.updateLocale(languageCode);
    }

    protected void updateLocale(Locale newLocale) {
        baseController.updateLocale(newLocale);
    }

    protected String getFullRequestURL() {
        return baseController.getFullRequestURL();
    }

    protected String getApplicationName() {
        return baseController.getApplicationName();
    }

    protected String getApplicationVersion() {
        return baseController.getApplicationVersion();
    }

    protected boolean isShowApplicationInfo() {
        return baseController.isShowApplicationInfo();
    }

    protected String getDefaultRedirect() {
        return baseController.getDefaultRedirect();
    }

    protected String getDefaultTemplateCode() {
        return baseController.getDefaultTemplateCode();
    }

    protected String getDefaultLanguageCode() {
        return baseController.getDefaultLanguageCode();
    }

    protected String createMessage(String code, Object[] arguments) {
        return baseController.createMessage(code, arguments);
    }

    protected String createMessage(String code) {
        return baseController.createMessage(code);
    }

    @SuppressWarnings("rawtypes")
    protected <T extends Map> ActionResult createActionResult(String status, String message, T data) {
        return baseController.createActionResult(status, message, data);
    }

    @SuppressWarnings("rawtypes")
    protected <T extends Map> ActionResult successActionResult(String message, T data) {
        return baseController.successActionResult(message, data);
    }

    @SuppressWarnings("rawtypes")
    protected <T extends Map> ActionResult successActionResult(T data) {
        return baseController.successActionResult(data);
    }

    protected ActionResult failActionResult(String message) {
        return baseController.failActionResult(message);
    }

    protected ActionResult errorActionResult() {
        return baseController.errorActionResult();
    }
        
    // Adapter for IBaseControllerUser
    
    protected ActionResult login(Map<String, Object> parameters) {
        return baseController.login(parameters);
    }

    protected ActionResult listUser(Map<String, Object> parameters) {
        return baseController.listUser(parameters);
    }

    protected ActionResult findUser(Map<String, Object> parameters) {
        return baseController.findUser(parameters);
    }

    protected ActionResult addUser(Map<String, Object> parameters) {
        return baseController.addUser(parameters);
    }

    protected ActionResult editUser(Map<String, Object> parameters) {
        return baseController.editUser(parameters);
    }

    protected ActionResult removeUser(Map<String, Object> parameters) {
        return baseController.removeUser(parameters);
    }

    protected ActionResult changePassword(Map<String, Object> parameters) {
        return baseController.changePassword(parameters);
    }

    protected JSONObject getLoggedInUser() {
        return baseController.getLoggedInUser();
    }

    protected boolean userHasLoggedIn() {
        return baseController.userHasLoggedIn();
    }
    
    // Adapter for IBaseControllerSystem

    protected JSONArray getSystems() {
        return baseController.getSystems();
    }

    protected String getSystem(String key) {
        return baseController.getSystem(key);
    }

    protected void loadSystems() {
        baseController.loadSystems();
    }

    protected ActionResult editSystems(Map<String, Object> parameters, String languageCode) {
        return baseController.editSystems(parameters, languageCode);
    }

    // Adapter for IBaseControllerUserGroup
    
    protected ActionResult listUserGroup(Map<String, Object> parameters) {
        return baseController.listUserGroup(parameters);
    }

    protected ActionResult findUserGroup(Map<String, Object> parameters) {
        return baseController.findUserGroup(parameters);
    }

    protected ActionResult addUserGroup(Map<String, Object> parameters) {
        return baseController.addUserGroup(parameters);
    }

    protected ActionResult editUserGroup(Map<String, Object> parameters) {
        return baseController.editUserGroup(parameters);
    } 

    protected ActionResult removeUserGroup(Map<String, Object> parameters) {
        return baseController.removeUserGroup(parameters);
    }

    protected JSONObject getLoggedInUserGroup() {
        return baseController.getLoggedInUserGroup();
    }

    // Adapter for IBaseControllerEmailQueue
    
    protected ActionResult addEmailQueue(Map<String, Object> parameters) {
        return baseController.addEmailQueue(parameters);
    }
    
    protected ActionResult getEmailQueuesByStatus(Integer status) {
        return baseController.getEmailQueuesByStatus(status);
    }
    
    protected ActionResult editEmailQueue(Map<String, Object> parameters) {
        return baseController.editEmailQueue(parameters);
    }
    
}
