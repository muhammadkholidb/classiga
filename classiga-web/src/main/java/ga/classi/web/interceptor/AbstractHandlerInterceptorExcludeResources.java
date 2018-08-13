/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

public abstract class AbstractHandlerInterceptorExcludeResources implements HandlerInterceptor {

    // http://stackoverflow.com/questions/18404906/spring-mvc-exclude-assets-from-all-interceptors

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return isResourceHandler(handler) ? true : doPreHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (!isResourceHandler(handler)) {
            doPostHandle(request, response, handler, modelAndView);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (!isResourceHandler(handler)) {
            doAfterCompletion(request, response, handler, ex);
        }
    }

    private boolean isResourceHandler(Object handler) {
        return handler instanceof ResourceHttpRequestHandler;
    }

    public abstract void doAfterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception;

    public abstract void doPostHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception;

    public abstract boolean doPreHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;

}
