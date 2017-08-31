package ga.classi.web.interceptor;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import ga.classi.commons.helper.CommonConstants;
import ga.classi.commons.helper.DefaultUser;
import ga.classi.web.helper.MenuKeyConstants;
import ga.classi.web.helper.SessionKeyConstants;
import ga.classi.web.helper.SessionManager;

public class PagePermissionInterceptor extends HandlerInterceptorAdapter {

    private static final Logger log = LoggerFactory.getLogger(PagePermissionInterceptor.class);

    @Autowired
    @Qualifier("applicationProp")
    protected Properties applicationProp;

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
        
        String menuCodeParent = null;
        String menuCode = null;
        
        JSONArray flatMenus = SessionManager.get(SessionKeyConstants.FLAT_MENUS);
        for (Object o : flatMenus) {
            JSONObject menu = (JSONObject) o;
            String path = (String) menu.get("path");
            if ((path != null) && requestUrl.contains(path)) {
                menuCode = (String) menu.get(MenuKeyConstants.CODE);
                menuCodeParent = (String) menu.get(MenuKeyConstants.PARENT_CODE);
                break;
            }
        }
        
        if (menuCode == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page requested is not found in the menus");
            return false;
        }
        
        log.debug("Menu code: {}", menuCode);
        log.debug("Menu code parent: {}", menuCodeParent);
        
        if (menuCodeParent != null && !menuCodeParent.isEmpty()) {
            // Get permission for the parent menu first
            if (!canView(menuCodeParent)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User is not allowed to view this page");
                return false;
            }
        }
        
        // Check permission for the real menu
        if (!canView(menuCode)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User is not allowed to view this page");
            return false;
        }
        
        return true;
    }

    private boolean canView(String code) {
        JSONObject currentMenu = getMenuPermission(code);
        log.debug("Current menu: {}", currentMenu);
        if (currentMenu == null || !CommonConstants.YES.equals(currentMenu.get("canView"))) {
            return false;
        }
        return true;
    }
    
    private JSONObject getMenuPermission(String code) {
        JSONArray menuPermissions = SessionManager.get(SessionKeyConstants.MENU_PERMISSIONS);
        for (Object o : menuPermissions) {
            JSONObject menu = (JSONObject) o;
            if (code.equals(menu.get("menuCode"))) {
                return menu;
            }
        }
        return null;
    }
    
}
