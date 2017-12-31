package ga.classi.web.helper;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.ui.context.Theme;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

// With modification from http://stackoverflow.com/questions/1629211/how-do-i-get-the-session-object-in-spring#answer-22087949
public final class WebContextHolder {

    private static final Logger log = LoggerFactory.getLogger(WebContextHolder.class);

    private WebContextHolder() {
        // Restrict instantiation
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attributes.getRequest();
    }

    public static HttpSession getSession() {
        return getSession(true);
    }

    public static HttpSession getSession(boolean create) {
        return getRequest().getSession(create);
    }

    public static String getSessionId() {
        return getSession().getId();
    }

    public static ServletContext getServletContext() {
        return getRequest().getServletContext();
    }

    public static Locale getLocale() {
        return RequestContextUtils.getLocale(getRequest());
    }

    public static Theme getTheme() {
        return RequestContextUtils.getTheme(getRequest());
    }

    public static ApplicationContext getApplicationContext() {
        return WebApplicationContextUtils.getWebApplicationContext(getServletContext());
    }

    public static ApplicationEventPublisher getApplicationEventPublisher() {
        return (ApplicationEventPublisher) getApplicationContext();
    }

    public static LocaleResolver getLocaleResolver() {
        return RequestContextUtils.getLocaleResolver(getRequest());
    }

    public static ThemeResolver getThemeResolver() {
        return RequestContextUtils.getThemeResolver(getRequest());
    }

    public static ResourceLoader getResourceLoader() {
        return (ResourceLoader) getApplicationContext();
    }

    public static ResourcePatternResolver getResourcePatternResolver() {
        return (ResourcePatternResolver) getApplicationContext();
    }

    public static MessageSource getMessageSource() {
        return getBeanFromApplicationContext(MessageSource.class);
    }

    public static ConversionService getConversionService() {
        return getBeanFromApplicationContext(ConversionService.class);
    }

    public static DataSource getDataSource() {
        return getBeanFromApplicationContext(DataSource.class);
    }

    public static Collection<String> getActiveProfiles() {
        return Arrays.asList(getApplicationContext().getEnvironment().getActiveProfiles());
    }

    public static ClassLoader getBeanClassLoader() {
        return ClassUtils.getDefaultClassLoader();
    }

    private static <T> T getBeanFromApplicationContext(Class<T> requiredType) {
        try {
            return getApplicationContext().getBean(requiredType);
        } catch (NoUniqueBeanDefinitionException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (NoSuchBeanDefinitionException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

}
