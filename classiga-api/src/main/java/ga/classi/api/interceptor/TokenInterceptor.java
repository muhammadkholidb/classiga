/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.api.interceptor;

import ga.classi.api.constant.RequestMappingConstants;
import ga.classi.commons.constant.RequestDataConstants;
import ga.classi.commons.data.error.Errors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author muhammad
 */
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        String contextPath = request.getContextPath();
        String token = request.getParameter(RequestDataConstants.TOKEN);
        if (StringUtils.isBlank(token)) {
            response.sendRedirect(contextPath + RequestMappingConstants.DATA_EXCEPTION + "?" + RequestDataConstants.CODE + "=" + Errors.USER_SESSION_NOT_FOUND.code());
            return false; 
        }
        
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        
    }
    
}
