package ga.classi.web.interceptor;

import java.net.URLEncoder;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import ga.classi.web.helper.SessionKeyConstants;
import ga.classi.web.helper.SessionManager;
import ga.classi.web.helper.URLParameterKeyContants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private static final String ROOT_PAGE = "/";
    private static final String LOGIN_PAGE = "/login";

    @Autowired
    @Qualifier("applicationProp")
    protected Properties applicationProp;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        log.debug("Started ...");
        
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

        log.debug("User has logged in: {}", loggedIn);
        
        if (loginRequest) {
            if (loggedIn) {
                response.sendRedirect(defaultRedirect);
                return false;
            } else {
                return true;
            }
        } else {
            if (loggedIn) {
                return true;
            } else {
                String redirectPath = requestUrl.substring(contextPath.length());
                if ("/".equals(redirectPath)) {
                    response.sendRedirect(loginUrl);
                } else {                    
                    response.sendRedirect(loginUrl + "?" + URLParameterKeyContants.REDIRECT + "=" + URLEncoder.encode(redirectPath, "UTF-8"));
                }
                return false;
            }
        }
    }

}
