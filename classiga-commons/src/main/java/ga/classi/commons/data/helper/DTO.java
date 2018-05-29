package ga.classi.commons.data.helper;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author eatonmunoz
 */
public class DTO extends HashMap<Object, Object> {

    private static final long serialVersionUID = 1L;

    public DTO() {
        super();
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public <T extends Map> DTO(T map) {
        super();
        if (map != null) {    
            putAll(map);   
        }
    }
    
    public DTO omit(String... keys) {
        for (String key : keys) {
            super.remove(key);
        }
        return this;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) super.get(key);
    }

    @SuppressWarnings("unchecked")
    public DTO getDTO(String key) {
        return new DTO((Map<? extends Object, ? extends Object>) super.get(key));
    }

    public String getString(String key) {
        return (String) super.get(key);
    }
    
    public String getStringValue(String key) {
        // Don't return "null" if the value is null
        return (super.get(key) == null) ? null : String.valueOf(super.get(key));
    }

    public Integer getInteger(String key) {
        return (Integer) super.get(key);
    }
    
    public Integer getIntegerValue(String key) {
        try {
            return Integer.parseInt(super.get(key) + "");
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Long getLong(String key) {
        return (Long) super.get(key);
    }
    
    public Long getLongValue(String key) {
        try {
            return Long.parseLong(super.get(key) + "");
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public DTO put(Object k, Object v) {
        super.put(k, v);
        return this;
    }
    
}
