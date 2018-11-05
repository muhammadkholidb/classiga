/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.api.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import ga.classi.api.constant.RequestMappingConstants;
import ga.classi.commons.constant.RequestDataConstants;
import ga.classi.commons.data.DTO;
import ga.classi.commons.data.error.DataException;
import ga.classi.commons.data.error.Errors;
import ga.classi.commons.web.utility.RequestInformation;
import ga.classi.data.service.UserSessionService;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author muhammad
 */
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private UserSessionService svcUserSession;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // Print request information
        new RequestInformation(request).printRequestInfo();
        
        String contextPath = request.getContextPath();
        String token = request.getParameter(RequestDataConstants.TOKEN);
        if (StringUtils.isBlank(token)) {
            response.sendRedirect(contextPath + RequestMappingConstants.DATA_EXCEPTION + "?" + RequestDataConstants.CODE + "=" + Errors.USER_SESSION_NOT_FOUND.code());
            return false;
        }

        try {
            svcUserSession.updateUserSession(new DTO().put(RequestDataConstants.TOKEN, token));
        } catch (DataException e) {
            log.error("Failed to update user session", e);
            response.sendRedirect(contextPath + RequestMappingConstants.DATA_EXCEPTION + "?" + RequestDataConstants.CODE + "=" + e.getCode());
            return false;
        } catch (Exception e) {
            log.error("An error occurred when updating user session", e);
            response.sendRedirect(contextPath + RequestMappingConstants.DATA_EXCEPTION + "?" + RequestDataConstants.CODE + "=" + Errors.UNKNOWN.code());
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
