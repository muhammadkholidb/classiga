/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.commons.data.error;

import java.util.Arrays;

public class DataException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private ExceptionCode code;
    private String message;
    private Object[] data;

    public DataException() {}
    
    public DataException(ExceptionCode code, String message) {
        this.code = code;
        this.message = message;
    }
        
    public DataException(ExceptionCode code, String message, Object[] data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
        
    @Override
    public String getMessage() {
    	return this.message;
    }

    public void setMessage(String message) {
    	this.message = message;
    }
    
    public ExceptionCode getCode() {
        return code;
    }

    public void setCode(ExceptionCode code) {
        this.code = code;
    }

    public Object[] getData() {
        return data;
    }

    public void setData(Object[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + ": " + code + " - " + message + ((data == null) ? "" : " - " + Arrays.asList(data)); 
    }
    
}
