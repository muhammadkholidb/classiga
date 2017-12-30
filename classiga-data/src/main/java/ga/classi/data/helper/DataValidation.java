package ga.classi.data.helper;

import java.util.Map;

import ga.classi.commons.data.error.DataException;
import ga.classi.commons.data.error.ExceptionCode;
import ga.classi.commons.helper.StringCheck;
import ga.classi.data.error.ErrorMessageConstants;

/**
 * Data validation helper class.
 * @author eatonmunoz
 */
public class DataValidation {

    private DataValidation() {
        // Set static class constructor to private
    }
    
    /**
     * Checks if the specified keys exist in the specified parameter.
     * @param parameter The parameter to check.
     * @param keys The keys which the existence to be checked in the parameter.
     * @throws DataException If some or all of the keys are not found in the parameter.
     */
    public static void containsRequiredData(Map<Object, Object> parameter, String... keys) {
        if (keys != null && keys.length > 0) {
            if (parameter == null || parameter.isEmpty()) {
                throw new DataException(ExceptionCode.E1004, ErrorMessageConstants.REQUIRED_PARAMETERS_NOT_FOUND);
            }
            for (String key : keys) {
                if (!parameter.containsKey(key) || (parameter.get(key) == null)) {
                    throw new DataException(ExceptionCode.E1004, ErrorMessageConstants.REQUIRED_PARAMETER_NOT_FOUND, new Object[]{key});
                }
            }
        }
    }

    /**
     * Validates the email.
     * @param email The email to validate.
     * @throws DataException If the email is not valid.
     */
    public static void validateEmail(String email) {
        validateEmpty(email, "Email");
        if (!StringCheck.isEmail(email)) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.INVALID_EMAIL_ADDRESS, new Object[]{email});
        }
    }

    /**
     * Validates the username. A valid username must not be null or empty, and have minimum length of 4.
     * @param username The username to validate.
     * @throws DataException If the username is not valid.
     */
    public static void validateUsername(String username) {
        validateEmpty(username, "Username");
        if ((username == null) || username.isEmpty()) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.EMPTY_USERNAME, new Object[]{username});
        }
        if (username.length() < 4) {
            throw new DataException(ExceptionCode.E1006, ErrorMessageConstants.USERNAME_TOO_SHORT, new Object[]{username});
        }
    }

    /**
     * Validates if the string is a number.
     * @param string The string to validate.
     * @param name The validated component name.
     * @throws DataException If the string is not a number.
     */
    public static void validateNumeric(String string, String name) {
        validateEmpty(string, name);
        if (!StringCheck.isNumeric(string)) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.INVALID_NUMERIC, new Object[]{string, name});
        }
    }

    /**
     * Validates if the string is a case insensitive "Y" or "N".
     * @param string The string to validate.
     * @param name The validated component name.
     * @throws DataException If the string is not a case insensitive "Y" or "N".
     */
    public static void validateYesNo(String string, String name) {
        validateEmpty(string, name);
        if (!StringCheck.isYesNo(string)) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.INVALID_YES_NO, new Object[]{string, name});
        }
    }

    /**
     * Validates if the string is JSON array.
     * @param string The string to validate.
     * @param name The validated component name.
     * @throws DataException If the string is not a valid JSON array.
     */
    public static void validateJSONArray(String string, String name) {
        validateEmpty(string, name);
        if (!StringCheck.isJSONArray(string)) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.INVALID_JSON_ARRAY, new Object[]{string, name});
        }
    }

    /**
     * Validates if the string is JSON object.
     * @param string The string to validate.
     * @param name The validated component name.
     * @throws DataException If the string is not a valid JSON object.
     */
    public static void validateJSONObject(String string, String name) {
        validateEmpty(string, name);
        if (!StringCheck.isJSONObject(string)) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.INVALID_JSON_OBJECT, new Object[]{string, name});
        }
    }

    /**
     * Validates if the string is not null or empty.
     * @param string The string to validate.
     * @param name The validated component name.
     * @throws DataException If the string is null or empty.
     */
    public static void validateEmpty(String string, String name) {
        if (StringCheck.isEmpty(string)) {
            throw new DataException(ExceptionCode.E1006, ErrorMessageConstants.EMPTY_VALUE, new Object[]{name});
        }
    }

}
