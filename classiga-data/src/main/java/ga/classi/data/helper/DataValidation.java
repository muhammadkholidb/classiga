/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.data.helper;

import java.util.List;
import java.util.Map;

import ga.classi.commons.data.error.DataException;
import ga.classi.commons.data.error.ExceptionCode;
import ga.classi.commons.helper.StringCheck;
import ga.classi.data.error.ErrorMessageConstants;

/**
 * Data validation helper class.
 * 
 * @author muhammad
 */
public class DataValidation {

    public static final int EMPTY                = 1;
    public static final int EMAIL                = 2;
    public static final int EMAIL_OR_EMPTY       = 3;
    public static final int NUMBER               = 4;
    public static final int NUMBER_OR_EMPTY      = 5;
    public static final int JSON_ARRAY           = 6;
    public static final int JSON_ARRAY_OR_EMPTY  = 7;
    public static final int JSON_OBJECT          = 8;
    public static final int JSON_OBJECT_OR_EMPTY = 9;
    public static final int YES_NO               = 10;
    public static final int YES_NO_OR_EMPTY      = 11;

    private DataValidation() {
        // Set static class constructor to private
    }

    public static void containsRequiredData(Map<Object, Object> parameter, String... keys) {
        if (keys != null && keys.length > 0) {
            if (parameter == null || parameter.isEmpty()) {
                throw new DataException(ExceptionCode.E1004, ErrorMessageConstants.REQUIRED_PARAMETERS_NOT_FOUND);
            }
            for (String key : keys) {
                if (!parameter.containsKey(key) || (parameter.get(key) == null)) {
                    throw new DataException(ExceptionCode.E1004, ErrorMessageConstants.REQUIRED_PARAMETER_NOT_FOUND, new Object[] { key });
                }
            }
        }
    }

    public static void validateEmail(Object object) {
        if ((object == null) || !StringCheck.isEmail(object.toString())) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.INVALID_EMAIL_ADDRESS, new Object[] { object });
        }
    }

    public static void validateEmailOrEmpty(Object object) {
        if (object == null) {
            return;
        }
        if (StringCheck.isEmpty(object.toString())) {
            return;
        }
        if (!StringCheck.isEmail(object.toString())) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.INVALID_EMAIL_ADDRESS, new Object[] { object });
        }
    }

    public static void validateUsername(String username) {
        if (username.length() < 4) {
            throw new DataException(ExceptionCode.E1006, ErrorMessageConstants.USERNAME_TOO_SHORT, new Object[] { username });
        }
    }

    public static void validateNumber(Object object, String name) {
        if ((object == null) || !StringCheck.isNumber(object.toString())) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.INVALID_NUMBER, new Object[] { object, name });
        }
    }

    public static void validateNumberOrEmpty(Object object, String name) {
        if (object == null) {
            return;
        }
        if (StringCheck.isEmpty(object.toString())) {
            return;
        }
        if (!StringCheck.isNumber(object.toString())) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.INVALID_NUMBER, new Object[] { object, name });
        }
    }

    public static void validateYesNo(Object object, String name) {
        if ((object == null) || !StringCheck.isYesNo(object.toString())) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.INVALID_YES_NO, new Object[] { object, name });
        }
    }

    public static void validateYesNoOrEmpty(Object object, String name) {
        if (object == null) {
            return;
        }
        if (StringCheck.isEmpty(object.toString())) {
            return;
        }
        if (!StringCheck.isYesNo(object.toString())) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.INVALID_YES_NO, new Object[] { object, name });
        }
    }

    public static void validateJSONArray(Object object, String name) {
        if ((object == null) || !StringCheck.isJSONArray(object.toString())) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.INVALID_JSON_ARRAY, new Object[] { object, name });
        }
    }

    public static void validateJSONArrayOrEmpty(Object object, String name) {
        if (object == null) {
            return;
        }
        if (StringCheck.isEmpty(object.toString())) {
            return;
        }
        if (!StringCheck.isJSONArray(object.toString())) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.INVALID_JSON_ARRAY, new Object[] { object, name });
        }
    }

    public static void validateJSONObject(Object object, String name) {
        if ((object == null) || !StringCheck.isJSONObject(object.toString())) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.INVALID_JSON_OBJECT, new Object[] { object, name });
        }
    }

    public static void validateJSONObjectOrEmpty(Object object, String name) {
        if (object == null) {
            return;
        }
        if (StringCheck.isEmpty(object.toString())) {
            return;
        }
        if (!StringCheck.isJSONObject(object.toString())) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.INVALID_JSON_OBJECT, new Object[] { object, name });
        }
    }

    public static void validateEmpty(String[] strings, String[] names) {
        for (int i = 0; i < strings.length; i++) {
            validateEmpty(strings[i], names[i]);
        }
    }

    @SuppressWarnings("rawtypes")
    public static void validateEmpty(Object object, String name) {
        if ((object == null) 
                || ((object instanceof List) && ((List) object).isEmpty())
                || ((object instanceof Map) && ((Map) object).isEmpty())
                || ((object instanceof String) && StringCheck.isEmpty(object.toString()))) {

            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.EMPTY_VALUE, new Object[] { name });
        }
    }

    public static void validateDate(String string, String pattern, String name) {
        if (!StringCheck.isDate(string, pattern)) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.INVALID_DATE, new Object[] { string, name });
        }
    }

    public static void validateEquals(String str1, String str2, String desc) {
        if (!str1.equals(str2)) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.VALUES_NOT_EQUALS, new Object[] { desc });
        }
    }

    public static void validate(Object object, String name, int... types) {
        
        for (int type : types) {
        
            switch (type) {

            case EMPTY:
                validateEmpty(object, name);
                break;

            case EMAIL:
                validateEmail(object);
                break;

            case EMAIL_OR_EMPTY:
                validateEmailOrEmpty(object);
                break;

            case NUMBER:
                validateNumber(object, name);
                break;

            case NUMBER_OR_EMPTY:
                validateNumberOrEmpty(object, name);
                break;

            case JSON_ARRAY:
                validateJSONArray(object, name);
                break;

            case JSON_ARRAY_OR_EMPTY:
                validateJSONArrayOrEmpty(object, name);
                break;

            case JSON_OBJECT:
                validateJSONObject(object, name);
                break;

            case JSON_OBJECT_OR_EMPTY:
                validateJSONObjectOrEmpty(object, name);
                break;

            case YES_NO:
                validateYesNo(object, name);
                break;

            case YES_NO_OR_EMPTY:
                validateYesNoOrEmpty(object, name);
                break;

            default:
                throw new IllegalArgumentException("Invalid type of validation");
            }
        }
    }

}
