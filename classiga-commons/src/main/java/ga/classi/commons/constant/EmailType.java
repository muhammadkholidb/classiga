/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.commons.constant;

import java.util.HashMap;
import java.util.Map;

public enum EmailType {

    USER_CREATED(1, "email.queue.type.subject.usercreated", "user-created");

    private final Integer id;
    private final String code;
    private final String template;

    EmailType(Integer id, String code, String template) {
        this.id = id;
        this.code = code;
        this.template = template;
    }
    
    public Integer id() {
        return this.id;
    }

    public String code() {
        return this.code;
    }
    
    public String template() {
        return this.template;
    }
    
    public static Integer[] ids() {
        EmailType[] values = values();
        Integer[] ids = new Integer[values.length];
        for (int i = 0; i < values.length; i++) {
            ids[i] = values[i].id;
        }
        return ids;
    }

    public static String[] codes() {
        EmailType[] values = values();
        String[] codes = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            codes[i] = values[i].code;
        }
        return codes;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object>[] maps() {
        EmailType[] values = values();
        Map<String, Object>[] maps = new Map[values.length];
        for (int i = 0; i < values.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", values[i].id);
            map.put("code", values[i].code);
            maps[i] = map;
        }
        return maps;
    }
    
    public static EmailType byId(Integer id) {
        for (EmailType status : values()) {
            if (status.id.equals(id)) {
                return status;
            }
        }
        return null;
    }
    
    public static boolean isValidId(Integer id) {
        return byId(id) != null;
    }
    
}
