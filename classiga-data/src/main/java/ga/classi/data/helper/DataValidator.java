/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.data.helper;

import ga.classi.commons.data.DTO;
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
public class DataValidator {

    private final DTO dtoInput;

    public DataValidator(DTO dtoInput) {
        this.dtoInput = dtoInput;
    }

    public void containsRequiredData(String... keys) {
        if (keys != null && keys.length > 0) {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (String key : keys) {
                if (!dtoInput.containsKey(key) || (dtoInput.get(key) == null)) {
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

    public String validateEmail(String field, String message) {
        String email = dtoInput.getStringValue(field);
        if ((email == null) || !StringCheck.isEmail(email)) {
            throw new DataException(Errors.INVALID_EMAIL_ADDRESS.code(), message, new Object[]{field});
        }
        return email;
    }

    public String validateEmail(String field) {
        return validateEmail(field, Errors.INVALID_EMAIL_ADDRESS.messageCode());
    }

    public String validateEmailOrEmpty(String field, String message) {
        String email = dtoInput.getStringValue(field);
        if (email == null) {
            return email;
        }
        if (StringCheck.isEmpty(email)) {
            return email;
        }
        if (!StringCheck.isEmail(email)) {
            throw new DataException(Errors.INVALID_EMAIL_ADDRESS, new Object[]{field});
        }
        return email;
    }

    public String validateEmailOrEmpty(String field) {
        return validateEmailOrEmpty(field, Errors.INVALID_EMAIL_ADDRESS.messageCode());
    }

    public String validateUsername(String field, String message) {
        String username = dtoInput.getStringValue(field);
        if (username.length() < 4) {
            throw new DataException(Errors.USERNAME_TOO_SHORT, new Object[]{username});
        }
        return username;
    }

    public String validateUsername(String field) {
        return validateUsername(field, Errors.USERNAME_TOO_SHORT.messageCode());
    }

    public String validateNumber(String field, String message) {
        String number = dtoInput.getStringValue(field);
        if ((number == null) || !StringCheck.isNumber(number)) {
            throw new DataException(Errors.INVALID_NUMBER.code(), message, new Object[]{field});
        }
        return number;
    }

    public String validateNumber(String field) {
        return validateNumber(field, Errors.INVALID_NUMBER.messageCode());
    }
    
    public String validateNumberOrEmpty(String field, String message) {
        String number = dtoInput.getStringValue(field);
        if (number == null) {
            return number;
        }
        if (StringCheck.isEmpty(number)) {
            return number;
        }
        if (!StringCheck.isNumber(number)) {
            throw new DataException(Errors.INVALID_NUMBER, new Object[]{field});
        }
        return number;
    }

    public String validateNumberOrEmpty(String field) {
        return validateNumberOrEmpty(field, Errors.INVALID_NUMBER.messageCode());
    }
    
    public String validateYesNo(String field, String message) {
        String yn = dtoInput.getStringValue(field);
        if ((yn == null) || !StringCheck.isYesNo(yn)) {
            throw new DataException(Errors.INVALID_YES_NO.code(), message, new Object[]{field});
        }
        return yn;
    }

    public String validateYesNo(String field) {
        return validateYesNo(field, Errors.INVALID_YES_NO.messageCode());
    }
    
    public String validateYesNoOrEmpty(String field, String message) {
        String yn = dtoInput.getStringValue(field);
        if (yn == null) {
            return yn;
        }
        if (StringCheck.isEmpty(yn)) {
            return yn;
        }
        if (!StringCheck.isYesNo(yn)) {
            throw new DataException(Errors.INVALID_YES_NO.code(), message, new Object[]{field});
        }
        return yn;
    }

    public String validateYesNoOrEmpty(String field) {
        return validateYesNoOrEmpty(field, Errors.INVALID_YES_NO.messageCode());
    }
    
    public String validateJSONArray(String field, String message) {
        String json = dtoInput.getStringValue(field);
        if ((json == null) || !StringCheck.isJSONArray(json)) {
            throw new DataException(Errors.INVALID_JSON_ARRAY.code(), message, new Object[]{field});
        }
        return json;
    }

    public String validateJSONArray(String field) {
        return validateJSONArray(field, Errors.INVALID_JSON_ARRAY.messageCode());
    }
    
    public String validateJSONArrayOrEmpty(String field, String message) {
        String json = dtoInput.getStringValue(field);
        if (json == null) {
            return json;
        }
        if (StringCheck.isEmpty(json)) {
            return json;
        }
        if (!StringCheck.isJSONArray(json)) {
            throw new DataException(Errors.INVALID_JSON_ARRAY.code(), message, new Object[]{field});
        }
        return json;
    }

    public String validateJSONArrayOrEmpty(String field) {
        return validateJSONArrayOrEmpty(field, Errors.INVALID_JSON_ARRAY.messageCode());
    }
    
    public String validateJSONObject(String field, String message) {
        String json = dtoInput.getStringValue(field);
        if ((json == null) || !StringCheck.isJSONObject(json)) {
            throw new DataException(Errors.INVALID_JSON_OBJECT.code(), message, new Object[]{field});
        }
        return json;
    }

    public String validateJSONObject(String field) {
        return validateJSONObject(field, Errors.INVALID_JSON_ARRAY.messageCode());
    }
    
    public String validateJSONObjectOrEmpty(String field, String message) {
        String json = dtoInput.getStringValue(field);
        if (json == null) {
            return json;
        }
        if (StringCheck.isEmpty(json)) {
            return json;
        }
        if (!StringCheck.isJSONObject(json)) {
            throw new DataException(Errors.INVALID_JSON_OBJECT.code(), message, new Object[]{field});
        }
        return json;
    }

    public String validateJSONObjectOrEmpty(String field) {
        return validateJSONObjectOrEmpty(field, Errors.INVALID_JSON_ARRAY.messageCode());
    }
    
    public Object[] validateEmpty(String[] fields, String[] messages) {
        if (fields == null || (fields.length != messages.length)) {
            throw new IllegalArgumentException("Fields and messages length must be the same");
        }
        Object[] values = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            values[i] = validateEmpty(fields[i], messages[i]);
        }
        return values;
    }

    @SuppressWarnings("rawtypes")
    public Object validateEmpty(String field, String message) {
        Object value = dtoInput.get(field);
        if ((value == null)
                || ((value instanceof List) && ((List) value).isEmpty())
                || ((value instanceof Map) && ((Map) value).isEmpty())
                || ((value instanceof String) && StringCheck.isEmpty(value.toString()))) {

            throw new DataException(Errors.EMPTY_VALUE.code(), message, new Object[]{field});
        }
        return value;
    }

    public Object validateEmpty(String field) {
        return validateEmpty(field, Errors.EMPTY_VALUE.messageCode());
    }

    public String validateEmptyString(String field) {
        return validateEmpty(field).toString();
    }
    
    public String validateDate(String field, String pattern, String message) {
        String date = dtoInput.getStringValue(field);
        if (!StringCheck.isDate(date, pattern)) {
            throw new DataException(Errors.INVALID_DATE.code(), message, new Object[]{field, pattern});
        }
        return date;
    }

    public String validateDate(String field, String pattern) {
        return validateDate(field, pattern, Errors.INVALID_DATE.messageCode());
    }

    public String[] validateEquals(String field1, String field2, String message) {
        String str1 = dtoInput.getStringValue(field1);
        String str2 = dtoInput.getStringValue(field2);
        if (!str1.equals(str2)) {
            throw new DataException(Errors.VALUES_NOT_EQUALS.code(), message, new Object[]{field1, field2});
        }
        return new String[] {str1, str2};
    }

    public String[] validateEquals(String field1, String field2) {
        return validateEquals(field1, field2, Errors.VALUES_NOT_EQUALS.messageCode());
    }

    public String validateURL(String field, String message) {
        String url = dtoInput.getStringValue(field);
        if ((url == null) || !StringCheck.isURL(url)) {
            throw new DataException(Errors.INVALID_URL.code(), message, new Object[]{field});
        }
        return url;
    }

    public String validateURL(String field) {
        return validateURL(field, Errors.INVALID_URL.messageCode());
    }

    public String validateURLOrEmpty(String field, String message) {
        String url = dtoInput.getStringValue(field);
        if (url == null) {
            return url;
        }
        if (StringCheck.isEmpty(url)) {
            return url;
        }
        if (!StringCheck.isEmail(url)) {
            throw new DataException(Errors.INVALID_URL, new Object[]{field});
        }
        return url;
    }

    public String validateURLOrEmpty(String field) {
        return validateURLOrEmpty(field, Errors.INVALID_URL.messageCode());
    }

}
