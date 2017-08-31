package ga.classi.rest.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author eatonmunoz
 *
 */
@Setter
@Getter
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
        this(status, message, null);
    }

}
