package ga.classi.commons.helper;

import java.util.List;
import java.util.Map;

import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.routines.EmailValidator;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A utility class for string checking.
 * @author eatonmunoz
 */
public final class StringCheck {
    
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
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
        try {
            return MAPPER.readValue(text, List.class) != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Returns true if the given text is in JSON object format.
     * @param text The string to check.
     * @return true if the given text is in JSON object format.
     */
    public static boolean isJSONObject(String text) {
        try {
            return MAPPER.readValue(text, Map.class) != null;
        } catch (Exception e) {
            return false;
        } 
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
    
}
