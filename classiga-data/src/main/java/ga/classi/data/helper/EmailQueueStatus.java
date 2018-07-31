package ga.classi.data.helper;

import java.util.HashMap;
import java.util.Map;

public enum EmailQueueStatus {

    PENDING(1, "email.queue.status.pending"),
    PROCESSING(2, "email.queue.status.processing"),
    DONE(3, "email.queue.status.done");

    private final Integer id;
    private final String code;

    EmailQueueStatus(Integer id, String code) {
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
        EmailQueueStatus[] values = values();
        Integer[] ids = new Integer[values.length];
        for (int i = 0; i < values.length; i++) {
            ids[i] = values[i].id;
        }
        return ids;
    }
    
    public static String[] codes() {
        EmailQueueStatus[] values = values();
        String[] codes = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            codes[i] = values[i].code;
        }
        return codes;
    }
    
    @SuppressWarnings("unchecked")
    public static Map<String, Object>[] maps() {
        EmailQueueStatus[] values = values();
        Map<String, Object>[] maps = new Map[values.length];
        for (int i = 0; i < values.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", values[i].id);
            map.put("code", values[i].code);
            maps[i] = map;
        }
        return maps;
    }
    
    public static EmailQueueStatus byId(Integer id) {
        for (EmailQueueStatus status : values()) {
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
