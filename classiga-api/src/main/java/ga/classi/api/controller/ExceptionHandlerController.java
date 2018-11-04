/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.api.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ga.classi.api.helper.ResponseObject;
import ga.classi.commons.data.error.DataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerController extends BaseController {

    @ExceptionHandler(DataException.class)
    @ResponseBody
    public ResponseObject handleDataException(DataException e) {
        log.error("Data exception caught! {}", e.toString());
        return ResponseObject.fail(e.getCode(), getResponseMessage(e.getMessage(), e.getData()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseObject handleOtherException(Exception e) {
        log.error("Exception caught!", e);
        return ResponseObject.error(getResponseMessage("error.ServerError")); 
    }

}
