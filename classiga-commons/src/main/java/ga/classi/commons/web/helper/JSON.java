package ga.classi.commons.web.helper;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Helper class for processing JSONObject and JSONArray.
 * @author Muhammad
 *
 */
@Slf4j
public class JSON {

    private JSON() {
        // Restrict instantiation
    }
    
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Get value from the specified JSONObject for the specified key. No need to cast the value.
     * @param <T> The return type.
     * @param key Key of the JSONObject to get the value from.
     * @param json The source of JSONObject.
     * @return The value from the JSONObject for specified key.
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(JSONObject json, String key) {
        if (json == null) {
            throw new NullPointerException("Cannot get from null JSONObject.");
        }
        return (T) json.get(key);
    }
    
    /**
     * Set JSONObject with the specified key and value.
     * @param key Key to set to the specified JSONObject.
     * @param value Value of the key.
     * @param json The source of JSONObject.
     */
    @SuppressWarnings("unchecked")
    public static void set(JSONObject json, String key, Object value) {
        if (json == null) {
            throw new NullPointerException("Cannot set to null JSONObject.");
        }
        json.put(key, value);
    }
    
    /**
     * Remove object for the specified key from the specified JSONObject.
     * @param keys Key of the object to remove.
     * @param json The source of JSONObject.
     * @return The source of JSONObject without the removed object.
     */
    public static JSONObject remove(JSONObject json, String... keys) {
        if (json == null) {
            throw new NullPointerException("Cannot remove from null JSONObject.");
        }
        for (String key : keys) {
            json.remove(key);   
        }
        return json;
    }
    
    /**
     * Get object in the specified index of the specified JSONArray. No need to cast the value. 
     * @param <T> The return type.
     * @param arr The source of JSONArray.
     * @param index The index of JSONArray to get the value from.
     * @return The value in the specified index of the JSONArray.
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(JSONArray arr, int index) {
        if (arr == null) {
            throw new NullPointerException("Cannot get from null JSONArray.");
        }
        return (T) arr.get(index);
    }
    
    public static JSONObject toJSONObject(Object object) {
        return MAPPER.convertValue(object, JSONObject.class);
    }
    
    public static String stringify(Object object) {
        return stringify(object, false);
    }
    
    public static String stringify(Object object, boolean indent) {
        try {
            if (indent) {
                return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            }
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.warn("Unable to stringify object");
        } 
        return null;
    }
    
    public static <T> T parse(String json, Class<T> type) {
        try {
            return MAPPER.readValue(json, type);
        } catch (Exception e) {
            log.warn("Unable to parse JSON: {}", json);
        }
        return null;
    }
    
}
