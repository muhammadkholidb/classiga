/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga.classi.data.helper;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author eatonmunoz
 */
public class Dto extends HashMap<Object, Object> {
    
    public Dto() {
        super();
    }
    
    public Dto(Map<? extends Object, ? extends Object> map) {
        super(map);
    }
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public Dto omit(String... keys) {
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
    public Dto getDto(String key) {
        return new Dto((Map<? extends Object, ? extends Object>) super.get(key));
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
        } catch (Exception e) {
            return null;
        }
    }

    public Long getLong(String key) {
        return (Long) super.get(key);
    }
    
    public Long getLongValue(String key) {
        try {
            return Long.parseLong(super.get(key) + "");
        } catch (Exception e) {
            return null;
        }
    }
            
    @Override
    public Dto put(Object k, Object v) {
        super.put(k, v);
        return this;
    }
    
}
