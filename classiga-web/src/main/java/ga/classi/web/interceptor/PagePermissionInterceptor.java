package ga.classi.web.interceptor;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import ga.classi.commons.helper.CommonConstants;
import ga.classi.commons.helper.DefaultUser;
import ga.classi.web.helper.MenuKeyConstants;
import ga.classi.web.helper.SessionKeyConstants;
import ga.classi.web.helper.SessionManager;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PagePermissionInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    @Qualifier("applicationProp")
    protected Properties applicationProp;

    private static final String PATH_ADD = "/add";
    private static final String PATH_EDIT = "/edit";
    private static final String PATH_REMOVE = "/remove";
    private static final String PATH_LIST = "/list";
    
    private static final List<String> ENDING_PATHS = Arrays.asList(PATH_ADD, PATH_EDIT, PATH_REMOVE, PATH_LIST);
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        log.debug("Pre handle ...");
        
        JSONObject loggedInUser = SessionManager.get(SessionKeyConstants.USER);
        
        if (loggedInUser == null || DefaultUser.USER_ID.equals(loggedInUser.get("id"))) {
            // User has not logged in or logged in using default user
            return true;
        }
        
        String requestUrl = request.getRequestURI();

        log.debug("Request URL: {}", requestUrl);
        
        JSONObject currentMenu = findInAvailableMenu(request, (JSONArray) SessionManager.get(SessionKeyConstants.FLAT_MENUS));
        
        if (currentMenu == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page requested is not found in the menus");
            return false;
        }
        
        String menuCode = (String) currentMenu.get(MenuKeyConstants.CODE);
        
        log.debug("Menu code: {}", menuCode);
        
        JSONArray menuPermissions = SessionManager.get(SessionKeyConstants.MENU_PERMISSIONS);

        if (!canView(menuCode, menuPermissions)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User is not allowed to view this page");
            return false;
        }

        if (requestUrl.contains(PATH_EDIT) 
                || requestUrl.contains(PATH_ADD) 
                || requestUrl.contains(PATH_REMOVE)) {
            
            if (!canModify(menuCode, menuPermissions)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User is not allowed to view this page");
                return false;
            }
        }
        
        return true;
    }

    private boolean canView(String code, JSONArray menuPermissions) {
        JSONObject currentMenu = findInAllowedMenu(code, menuPermissions);
        log.debug("Current menu: {}", currentMenu);
        return !(currentMenu == null || !CommonConstants.YES.equals(currentMenu.get("canView")));
    }
    
    private boolean canModify(String code, JSONArray menuPermissions) {
        JSONObject currentMenu = findInAllowedMenu(code, menuPermissions);
        log.debug("Current menu: {}", currentMenu);
        return !(currentMenu == null || !CommonConstants.YES.equals(currentMenu.get("canModify")));
    }
    
    private JSONObject findInAllowedMenu(String code, JSONArray menuPermissions) {
        for (Object o : menuPermissions) {
            JSONObject menu = (JSONObject) o;
            if (code.equals(menu.get("menuCode"))) {
                return menu;
            }
        }
        return null;
    }
 
    private JSONObject findInAvailableMenu(HttpServletRequest httpServletRequest, JSONArray flatMenus) {
        String contextPath = httpServletRequest.getContextPath();
        String requestUrl = httpServletRequest.getRequestURI(); // This method returns the path before URL query strings
        for (String path : ENDING_PATHS) {
            if (requestUrl.contains(path)) {
                requestUrl = requestUrl.substring(0, requestUrl.indexOf(path));
                break;
            }
        }
        for (Object o : flatMenus) {
            JSONObject menu = (JSONObject) o; 
            String path = (String) menu.get(MenuKeyConstants.PATH);
            if (path != null && requestUrl.substring(contextPath.length(), requestUrl.length()).equals(path)) {
                return menu;
            }
        }
        return null;
    }
       
}
