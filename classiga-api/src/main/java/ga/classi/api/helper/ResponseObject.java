/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.api.helper;

import ga.classi.commons.constant.CommonConstants;
import ga.classi.commons.constant.StringConstants;
import ga.classi.commons.data.error.Errors;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author muhammad
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseObject {

    private String status;
    private String code;
    private String message;
    private Object data;

    public static ResponseObject success(String message, Object data) {
        return new ResponseObject(CommonConstants.SUCCESS, Errors.NONE.code(), message, data);
    }
 
    public static ResponseObject success(String message) {
        return success(message, Collections.EMPTY_MAP);
    }
    
    public static ResponseObject success(Object data) {
        return success(StringConstants.EMPTY, data);
    }
    
    public static ResponseObject success() {
        return success(StringConstants.EMPTY);
    }
    
    public static ResponseObject fail(String code, String message) {
        return new ResponseObject(CommonConstants.FAIL, code, message, Collections.EMPTY_MAP);
    }
    
    public static ResponseObject error(String message) {
        return new ResponseObject(CommonConstants.ERROR, Errors.UNKNOWN.code(), message, Collections.EMPTY_MAP);
    }
    
}
