/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.commons.data.error;

import java.util.Arrays;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String code;
    private String message;
    private Object[] data;

    public DataException(String code, String message, Object[] data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    public DataException(String code, String message) {
        this(code, message, new Object[0]);
    }
    
    public DataException(Errors errorCode, Object[] data) {
        this(errorCode.code(), errorCode.messageCode(), data);
    }
    
    public DataException(Errors errorCode) {
        this(errorCode, new Object[0]);
    }
    
    @Override
    public String toString() {
        return this.getClass().getName() + ": " + this.code + " - " + this.message + ((data == null) ? "" : " - " + Arrays.toString(data)); 
    }

}