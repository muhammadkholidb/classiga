package ga.classi.web.controller.base;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.simple.JSONArray;
import org.springframework.web.servlet.ModelAndView;

import ga.classi.commons.helper.ActionResult;

/**
 *
 * @author eatonmunoz
 */
public interface IBaseController extends IBaseControllerSystem, IBaseControllerUser, IBaseControllerUserGroup {

    // Package private access

    ModelAndView view(ModelAndView mav);

    ModelAndView view(String view, Map<String, Object> model);

    ModelAndView view(String view);

    ModelAndView viewAndNotify(ModelAndView mav, String message, String notificationType);

    ModelAndView viewAndNotify(String view, Map<String, Object> model, String message, String notificationType);

    ModelAndView viewAndNotify(String view, String message, String notificationType);

    ModelAndView viewAndNotifyError(ModelAndView mav, String message);

    ModelAndView viewAndNotifyError(String view, Map<String, Object> model, String message);

    ModelAndView viewAndNotifyError(String view, String message);

    ModelAndView viewAndNotifyInfo(ModelAndView mav, String message);

    ModelAndView viewAndNotifyInfo(String view, Map<String, Object> model, String message);

    ModelAndView viewAndNotifyInfo(String view, String message);

    ModelAndView viewAndNotifyWarning(ModelAndView mav, String message);

    ModelAndView viewAndNotifyWarning(String view, Map<String, Object> model, String message);

    ModelAndView viewAndNotifyWarning(String view, String message);

    ModelAndView viewAndNotifySuccess(ModelAndView mav, String message);

    ModelAndView viewAndNotifySuccess(String view, Map<String, Object> model, String message);

    ModelAndView viewAndNotifySuccess(String view, String message);

    ModelAndView redirect(String path);

    ModelAndView redirect(String path, HashMap<String, Object> flashModel);

    ModelAndView redirectAndNotify(String path, String message, String notificationType);

    ModelAndView redirectAndNotify(String path, HashMap<String, Object> flashModel, String message, String notificationType);

    ModelAndView redirectAndNotifyError(String path, String message);

    ModelAndView redirectAndNotifyError(String path, HashMap<String, Object> flashModel, String message);

    ModelAndView redirectAndNotifyInfo(String path, String message);

    ModelAndView redirectAndNotifyInfo(String path, HashMap<String, Object> flashModel, String message);

    ModelAndView redirectAndNotifySuccess(String path, String message);

    ModelAndView redirectAndNotifySuccess(String path, HashMap<String, Object> flashModel, String message);

    ModelAndView redirectAndNotifyWarning(String path, String message);

    ModelAndView redirectAndNotifyWarning(String path, HashMap<String, Object> flashModel, String message);

    JSONArray getMenus(boolean flat);

    JSONArray getMenus();

    JSONArray getAllowedMenus();

    void loadAllowedMenus();
    
    boolean isMenuAllowed(String can, String code);

    boolean isMenuViewAllowed(String code);
    
    boolean isMenuModifyAllowed(String code);

    List<Locale> getSupportedLocales(String key);

    List<String> getAvailableTemplates();
    
    boolean isGet();

    boolean isPost();

    void updateLocale(String languageCode);

    void updateLocale(Locale newLocale);

    String getFullRequestURL();

    String getApplicationName();

    String getApplicationVersion();

    boolean isShowApplicationInfo();

    String getDefaultRedirect();

    String getDefaultTemplateCode();

    String getDefaultLanguageCode();

    String createMessage(String code, Object[] arguments);

    String createMessage(String code);

    @SuppressWarnings("rawtypes")
    <T extends Map> ActionResult createActionResult(String status, String message, T data);
    
    @SuppressWarnings("rawtypes")
    <T extends Map> ActionResult successActionResult(String message, T data);

    @SuppressWarnings("rawtypes")
    <T extends Map> ActionResult successActionResult(T data); 
    
    ActionResult failActionResult(String message);

    ActionResult errorActionResult();
    
}
