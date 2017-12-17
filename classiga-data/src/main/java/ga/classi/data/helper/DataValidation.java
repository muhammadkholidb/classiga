package ga.classi.data.helper;

import java.util.Map;

import ga.classi.commons.data.error.DataException;
import ga.classi.commons.data.error.ExceptionCode;
import ga.classi.commons.helper.StringCheck;
import ga.classi.data.error.ErrorMessageConstants;

/**
 *
 * @author eatonmunoz
 */
public class DataValidation {

    /**
     *
     * @param map
     * @param keys
     * @throws DataException
     */
    public static void containsRequiredData(Map<Object, Object> map, String... keys) throws DataException {
        if (keys != null && keys.length > 0) {
            if (map == null || map.isEmpty()) {
                throw new DataException(ExceptionCode.E1004, ErrorMessageConstants.REQUIRED_PARAMETERS_NOT_FOUND);
            }
            for (String key : keys) {
                if (!map.containsKey(key) || (map.get(key) == null)) {
                    throw new DataException(ExceptionCode.E1004, ErrorMessageConstants.REQUIRED_PARAMETER_NOT_FOUND, new Object[]{key});
                }
            }
        }
    }

    /**
     *
     * @param email
     * @throws DataException
     */
    public static void validateEmail(String email) throws DataException {
        validateEmpty(email, "Email");
        if (!StringCheck.isEmail(email)) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.INVALID_EMAIL_ADDRESS, new Object[]{email});
        }
    }

    /**
     *
     * @param username
     * @throws DataException
     */
    public static void validateUsername(String username) throws DataException {
        validateEmpty(username, "Username");
        if ((username == null) || username.isEmpty()) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.EMPTY_USERNAME, new Object[]{username});
        }
        if (username.length() < 4) {
            throw new DataException(ExceptionCode.E1006, ErrorMessageConstants.USERNAME_TOO_SHORT, new Object[]{username});
        }
    }

    /**
     *
     * @param string
     * @param name
     * @throws DataException
     */
    public static void validateNumeric(String string, String name) throws DataException {
        validateEmpty(string, name);
        if (!StringCheck.isNumeric(string)) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.INVALID_NUMERIC, new Object[]{string, name});
        }
    }

    /**
     *
     * @param string
     * @param name
     * @throws DataException
     */
    public static void validateYesNo(String string, String name) throws DataException {
        validateEmpty(string, name);
        if (!StringCheck.isYesNo(string)) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.INVALID_YES_NO, new Object[]{string, name});
        }
    }

    /**
     *
     * @param string
     * @param name
     * @throws DataException
     */
    public static void validateJSONArray(String string, String name) throws DataException {
        validateEmpty(string, name);
        if (!StringCheck.isJSONArray(string)) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.INVALID_JSON_ARRAY, new Object[]{string, name});
        }
    }

    /**
     *
     * @param string
     * @param name
     * @throws DataException
     */
    public static void validateJSONObject(String string, String name) throws DataException {
        validateEmpty(string, name);
        if (!StringCheck.isJSONObject(string)) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.INVALID_JSON_OBJECT, new Object[]{string, name});
        }
    }

    /**
     *
     * @param string
     * @param name
     * @throws DataException
     */
    public static void validateEmpty(String string, String name) throws DataException {
        if (StringCheck.isEmpty(string)) {
            throw new DataException(ExceptionCode.E1006, ErrorMessageConstants.EMPTY_VALUE, new Object[]{name});
        }
    }

}
