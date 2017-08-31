package ga.classi.commons.helper;

import org.apache.commons.validator.GenericValidator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author eatonmunoz
 */
public class StringCheck {
    
    /**
     * Returns true if the given text is null or empty.
     * @param text String to check.
     * @return true if the given text is null or empty.
     */
    public static boolean isEmpty(String text) {
        return text == null || text.isEmpty();
    }
    
    /**
     * 
     * @param text
     * @return 
     */
    public static boolean isNumeric(String text) {
        return GenericValidator.isLong(text) || GenericValidator.isDouble(text);
    } 
    
    /**
     * 
     * @param text
     * @return 
     */
    public static boolean isJSONArray(String text) {
        return JSONValue.parse(text) instanceof JSONArray;
    }
    
    /**
     * 
     * @param text
     * @return 
     */
    public static boolean isJSONObject(String text) {
        return JSONValue.parse(text) instanceof JSONObject;
    }
    
    /**
     * 
     * @param text
     * @return 
     */
    public static boolean isYesNo(String text) {
        return CommonConstants.YES.equalsIgnoreCase(text) || CommonConstants.NO.equalsIgnoreCase(text);
    }
    
    /**
     * 
     * @param text
     * @return 
     */
    public static boolean isEmail(String text) {
        return GenericValidator.isEmail(text);
    }
    
    /**
     * 
     * @param text
     * @param pattern
     * @return 
     */
    public static boolean isDate(String text, String pattern) {
        return GenericValidator.isDate(text, pattern, true);
    }
    
    /**
     * 
     * @param text
     * @return 
     */
    public static boolean isCreditCard(String text) {
        return GenericValidator.isCreditCard(text);
    }
    
}
