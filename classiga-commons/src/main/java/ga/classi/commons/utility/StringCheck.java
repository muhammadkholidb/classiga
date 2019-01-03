/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.commons.utility;

import ga.classi.commons.constant.CommonConstants;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * A utility class for string checking.
 * @author muhammad
 */
public final class StringCheck {
    
    private StringCheck() {
        // Restrict instantiation
    }
    
    /**
     * Returns true if the given text is null or empty (trimmed).
     * @param text The string to check.
     * @return true if the given text is null or empty (trimmed).
     */
    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }
    
    /**
     * Returns true if the given text is a number of long or double.
     * @param text The string to check.
     * @return true if the given text is a number of long or double.
     */
    public static boolean isNumber(String text) {
        return GenericValidator.isLong(text) || GenericValidator.isDouble(text);
    } 

    /**
     * Returns true if the given text contains only digit characters.
     * @param text The string to check.
     * @return true if the given text contains only digit characters.
     */
    public static boolean isDigits(String text) {
        return NumberUtils.isDigits(text);
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
     * Returns true if the given text is in email format. Local address without TLD like "@localhost" is allowed.
     * @param text The string to check.
     * @return true if the given text is in email format.
     */
    public static boolean isEmail(String text) {
        return EmailValidator.getInstance(true).isValid(text);
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
    
    /**
     * Returns true if the given text is a valid URL.
     * @param text The string to check.
     * @return true if the given text is a valid URL.
     */
    public static boolean isURL(String text) {
        return GenericValidator.isUrl(text);
    }
    
}
