/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.commons.data.constant;

import java.util.HashMap;
import java.util.Map;

public enum QueueStatus {

    PENDING(1, "queue.status.pending"),
    PROCESSING(2, "queue.status.processing"),
    DONE(3, "queue.status.done");

    private final Integer id;
    private final String code;

    QueueStatus(Integer id, String code) {
        this.id = id;
        this.code = code;
    }
    
    public Integer id() {
        return this.id;
    }
    
    public String code() {
        return this.code;
    }
    
    public static Integer[] ids() {
        QueueStatus[] values = values();
        Integer[] ids = new Integer[values.length];
        for (int i = 0; i < values.length; i++) {
            ids[i] = values[i].id;
        }
        return ids;
    }
    
    public static String[] codes() {
        QueueStatus[] values = values();
        String[] codes = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            codes[i] = values[i].code;
        }
        return codes;
    }
    
    @SuppressWarnings("unchecked")
    public static Map<String, Object>[] maps() {
        QueueStatus[] values = values();
        Map<String, Object>[] maps = new Map[values.length];
        for (int i = 0; i < values.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", values[i].id);
            map.put("code", values[i].code);
            maps[i] = map;
        }
        return maps;
    }
    
    public static QueueStatus byId(Integer id) {
        for (QueueStatus status : values()) {
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
