/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.data.helper;

import java.util.List;
import java.util.Map;

import ga.classi.commons.data.error.DataException;
import ga.classi.commons.data.error.Errors;
import ga.classi.commons.utility.StringCheck;

/**
 * Data validation helper class.
 *
 * @author muhammad
 */
public class DataValidation {

    private final Map<Object, ?> parameter;

    public DataValidation(Map<Object, ?> parameter) {
        this.parameter = parameter;
    }

    public void containsRequiredData(String... keys) {
        if (keys != null && keys.length > 0) {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (String key : keys) {
                if (!parameter.containsKey(key) || (parameter.get(key) == null)) {
                    sb.append(key);
                    if (i < keys.length) {
                        sb.append(", ");
                    }
                }
                i++;
            }
            if (sb.length() > 0) {
                throw new DataException(Errors.REQUIRED_PARAMETERS_NOT_FOUND, new Object[]{sb.toString()});
            }
        }
    }

    public void validateEmail(String field, String message) {
        Object object = parameter.get(field);
        if ((object == null) || !StringCheck.isEmail(object.toString())) {
            throw new DataException(Errors.INVALID_EMAIL_ADDRESS.code(), message, new Object[]{field});
        }
    }

    public void validateEmail(String field) {
        validateEmail(field, Errors.INVALID_EMAIL_ADDRESS.messageCode());
    }

    public void validateEmailOrEmpty(String field, String message) {
        Object object = parameter.get(field);
        if (object == null) {
            return;
        }
        if (StringCheck.isEmpty(object.toString())) {
            return;
        }
        if (!StringCheck.isEmail(object.toString())) {
            throw new DataException(Errors.INVALID_EMAIL_ADDRESS, new Object[]{field});
        }
    }

    public void validateEmailOrEmpty(String field) {
        validateEmailOrEmpty(field, Errors.INVALID_EMAIL_ADDRESS.messageCode());
    }

    public void validateUsername(String field, String message) {
        String username = String.valueOf(parameter.get(field));
        if (username.length() < 4) {
            throw new DataException(Errors.USERNAME_TOO_SHORT, new Object[]{username});
        }
    }

    public void validateUsername(String field) {
        validateUsername(field, Errors.USERNAME_TOO_SHORT.messageCode());
    }

    public static void validateNumber(Object object, String name) {
        if ((object == null) || !StringCheck.isNumber(object.toString())) {
            throw new DataException(Errors.INVALID_NUMBER, new Object[]{object, name});
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
            throw new DataException(Errors.INVALID_NUMBER, new Object[]{object, name});
        }
    }

    public static void validateYesNo(Object object, String name) {
        if ((object == null) || !StringCheck.isYesNo(object.toString())) {
            throw new DataException(Errors.INVALID_YES_NO, new Object[]{object, name});
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
            throw new DataException(Errors.INVALID_YES_NO, new Object[]{object, name});
        }
    }

    public static void validateJSONArray(Object object, String name) {
        if ((object == null) || !StringCheck.isJSONArray(object.toString())) {
            throw new DataException(Errors.INVALID_JSON_ARRAY, new Object[]{object, name});
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
            throw new DataException(Errors.INVALID_JSON_ARRAY, new Object[]{object, name});
        }
    }

    public static void validateJSONObject(Object object, String name) {
        if ((object == null) || !StringCheck.isJSONObject(object.toString())) {
            throw new DataException(Errors.INVALID_JSON_OBJECT, new Object[]{object, name});
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
            throw new DataException(Errors.INVALID_JSON_OBJECT, new Object[]{object, name});
        }
    }

    public static void validateEmpty(String[] strings, String[] names) {
        for (int i = 0; i < strings.length; i++) {
            validateEmpty(strings[i], names[i]);
        }
    }

    @SuppressWarnings("rawtypes")
    public static void validateEmpty(Object object, String field, String message) {
        if ((object == null)
                || ((object instanceof List) && ((List) object).isEmpty())
                || ((object instanceof Map) && ((Map) object).isEmpty())
                || ((object instanceof String) && StringCheck.isEmpty(object.toString()))) {

            throw new DataException(Errors.EMPTY_VALUE.code(), message, new Object[]{field});
        }
    }

    public static void validateEmpty(Object object, String field) {
        validateEmpty(object, field, Errors.EMPTY_VALUE.messageCode());
    }

    public static void validateDate(String string, String pattern, String field, String message) {
        if (!StringCheck.isDate(string, pattern)) {
            throw new DataException(Errors.INVALID_DATE.code(), message, new Object[]{field, string, pattern});
        }
    }

    public static void validateDate(String string, String pattern, String field) {
        validateDate(string, pattern, field, Errors.INVALID_DATE.messageCode());
    }

    public static void validateEquals(String str1, String str2, String field1, String field2, String message) {
        if (!str1.equals(str2)) {
            throw new DataException(Errors.VALUES_NOT_EQUALS.code(), message, new Object[]{field1, field2});
        }
    }

    public static void validateEquals(String str1, String str2, String field1, String field2) {
        validateEquals(str1, str2, field1, field2, Errors.VALUES_NOT_EQUALS.messageCode());
    }

}
