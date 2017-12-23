package ga.classi.web.helper;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author eatonmunoz
 */
public class SessionManager {

    private SessionManager() {}
    
    /**
     * Returns the session of current request, create one if the request has not had it yet.
     * @return The session of current request.
     */
    public static HttpSession getSession() {
        return getSession(true);
    }

    public static HttpSession getSession(boolean create) {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession(create);
    }
    
    public static String getId() {
        return getSession().getId();
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        return (T) getSession().getAttribute(key);
    } 
    
    public static void set(String key, Object value) {
        getSession().setAttribute(key, value);
    }
    
    public static void remove(String key) {
        getSession().removeAttribute(key);
    }
    
    public static void removeAll() {
        HttpSession session = getSession();
        Enumeration<String> keys = session.getAttributeNames();
        while (keys.hasMoreElements()) {
            session.removeAttribute(keys.nextElement());
        }
    }
    
    public static List<String> getKeys() {
        return Collections.list(getSession().getAttributeNames());
    }
    
    public static Map<String, Object> getMap() {
        HttpSession session = getSession();
        Map<String, Object> map = new HashMap<String, Object>();
        Enumeration<String> keys = session.getAttributeNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            map.put(key, session.getAttribute(key));
        }
        return map;
    }
    
    public static void destroy() {
        removeAll();
        getSession().invalidate();
    }
    
}
