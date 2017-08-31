package ga.classi.web.interceptor;

import java.net.URLEncoder;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import ga.classi.web.helper.SessionKeyConstants;
import ga.classi.web.helper.SessionManager;
import ga.classi.web.helper.URLParameterKeyContants;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);

    private static final String ROOT_PAGE = "/";
    private static final String LOGIN_PAGE = "/login";

    @Autowired
    @Qualifier("applicationProp")
    protected Properties applicationProp;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        log.debug("Pre handle ...");
        
        String requestUrl = request.getRequestURI();
        String contextPath = request.getContextPath();
        String rootUrl = contextPath + ROOT_PAGE;
        String loginUrl = contextPath + LOGIN_PAGE;

        log.debug("Request URL: {}", requestUrl);

        String defaultRedirect = contextPath + applicationProp.getProperty("login.redirect.path.default");
        
        boolean loggedIn = SessionManager.get(SessionKeyConstants.USER) != null;
        boolean loginRequest = requestUrl.equals(loginUrl) 
                || requestUrl.equals(loginUrl + "/") 
                || requestUrl.equals(rootUrl);

        // Check if the page is login request
        if (loginRequest) {
            // If the user has logged in, then redirect to home page
            if (loggedIn) {
                response.sendRedirect(defaultRedirect);
            } 
            // If the user has not logged in, then continue the request
            else {
                return true;
            }
        } 

        // Check if the page is not login request
        else {
            // If the user has logged in, then continue the request
            if (loggedIn) {
                return true;
            } 
            // If the user has not logged in, then redirect to login page with "redirect" parameter
            else {
                String redirectPath = requestUrl.substring(contextPath.length());
                if ("/".equals(redirectPath)) {
                    response.sendRedirect(loginUrl);
                } else {                    
                    response.sendRedirect(loginUrl + "?" + URLParameterKeyContants.REDIRECT + "=" + URLEncoder.encode(redirectPath, "UTF-8"));
                }
            }
        }
        
        return true;
    }

}
