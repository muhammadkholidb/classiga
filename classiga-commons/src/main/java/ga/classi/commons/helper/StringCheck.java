package ga.classi.commons.helper;

import org.apache.commons.validator.GenericValidator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * A utility class for string checking.
 * @author eatonmunoz
 */
public final class StringCheck {
    
    private StringCheck() {
        // Restrict instantiation
    }
    
    /**
     * Returns true if the given text is null or empty.
     * @param text The string to check.
     * @return true if the given text is null or empty.
     */
    public static boolean isEmpty(String text) {
        return text == null || text.isEmpty();
    }
    
    /**
     * Returns true if the given text is a number of integer or double.
     * @param text The string to check.
     * @return true if the given text is a number of integer or double.
     */
    public static boolean isNumeric(String text) {
        return GenericValidator.isLong(text) || GenericValidator.isDouble(text);
    } 
    
    /**
     * Returns true if the given text is in JSON array format.
     * @param text The string to check.
     * @return true if the given text is in JSON array format.
     */
    public static boolean isJSONArray(String text) {
        return JSONValue.parse(text) instanceof JSONArray;
    }
    
    /**
     * Returns true if the given text is in JSON object format.
     * @param text The string to check.
     * @return true if the given text is in JSON object format.
     */
    public static boolean isJSONObject(String text) {
        return JSONValue.parse(text) instanceof JSONObject;
    }
    
    /**
     * Returns true if the given text is a case insensitive "Y" or "N".
     * @param text The string to check.
     * @return true if the given text is a case insensitive "Y" or "N".
     */
    public static boolean isYesNo(String text) {
        return CommonConstants.YES.equalsIgnoreCase(text) || CommonConstants.NO.equalsIgnoreCase(text);
    }
    
    /**
     * Returns true if the given text is in email format.
     * @param text The string to check.
     * @return true if the given text is in email format.
     */
    public static boolean isEmail(String text) {
        return GenericValidator.isEmail(text);
    }
    
    /**
     * Returns true if the given text is in the specified date pattern.
     * @param text The string to check.
     * @param pattern The date pattern to be used for the checking.
     * @return true if the given text is in the specified date pattern.
     */
    public static boolean isDate(String text, String pattern) {
        return GenericValidator.isDate(text, pattern, true);
    }
    
    /**
     * Returns true if the given text is a credit card number.
     * @param text The string to check.
     * @return true if the given text is a credit card number.
     */
    public static boolean isCreditCard(String text) {
        return GenericValidator.isCreditCard(text);
    }
    
}
