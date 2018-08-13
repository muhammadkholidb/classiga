/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.web.interceptor;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import ga.classi.commons.constant.CommonConstants;
import ga.classi.commons.utility.DefaultUser;
import ga.classi.web.helper.SessionManager;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import ga.classi.web.constant.MenuConstants;
import ga.classi.web.constant.SessionConstants;

@Slf4j
public class PagePermissionInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    @Qualifier("applicationProp")
    protected Properties applicationProp;

    private static final String       PATH_ROOT    = "/";
    private static final String       PATH_ADD     = "/add";
    private static final String       PATH_EDIT    = "/edit";
    private static final String       PATH_REMOVE  = "/remove";
    private static final String       PATH_LIST    = "/list";

    private static final List<String> ENDING_PATHS = Arrays.asList(PATH_ADD, PATH_EDIT, PATH_REMOVE, PATH_LIST);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        log.debug("Started ...");
        
        JSONObject loggedInUser = SessionManager.get(SessionConstants.USER);
        
        if ((loggedInUser == null) || DefaultUser.USER_ID.equals(loggedInUser.get("id"))) {
            log.debug("User has not logged in or logged in as default user, continue ...");
            return true;
        }
        
        String requestUrl = request.getRequestURI();
        String contextPath = request.getContextPath();

        log.debug("Request URL: {}", requestUrl);

        if (requestUrl.equals(contextPath + PATH_ROOT)) {
            return true;
        }
        
        JSONArray flatMenus = SessionManager.get(SessionConstants.FLAT_MENUS);
        JSONObject currentMenu = findInAvailableMenu(request, flatMenus);
        
        if (currentMenu == null) {
            log.error("Page requested is not found in the menus");
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page requested is not found in the menus");
            return false;
        }
        
        String menuCode = (String) currentMenu.get(MenuConstants.CODE);
        
        log.debug("Menu code: {}", menuCode);
        
        JSONArray menuPermissions = SessionManager.get(SessionConstants.MENU_PERMISSIONS);

        if (!canView(menuCode, menuPermissions)) {
            log.error("User is not allowed to view this page");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User is not allowed to view this page");
            return false;
        }

        if ((requestUrl.contains(PATH_EDIT) 
                || requestUrl.contains(PATH_ADD) 
                || requestUrl.contains(PATH_REMOVE)) && !canModify(menuCode, menuPermissions)) {
            
            log.error("User is not allowed to view or modify this page");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User is not allowed to view or modify this page");
            return false;
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
            String path = (String) menu.get(MenuConstants.PATH);
            if (path != null && requestUrl.substring(contextPath.length(), requestUrl.length()).equals(path)) {
                return menu;
            }
        }
        return null;
    }
       
}
