/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.api.helper;

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
    private String message;
    private Object data;

    /**
     * Constructs ResponseObject with status and data.
     *
     * @param status Response status: success / fail.
     * @param data Response data when success.
     */
    public ResponseObject(String status, Object data) {
        this(status, "", data);
    }

    /**
     * Constructs ResponseObject with status and message.
     *
     * @param status Response status: success / fail.
     * @param message Response message when fail.
     */
    public ResponseObject(String status, String message) {
        this(status, message, Collections.EMPTY_MAP);
    }

}
