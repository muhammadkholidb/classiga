/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.api.controller;

import java.util.Collections;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ga.classi.api.helper.ResponseObject;
import ga.classi.commons.constant.CommonConstants;
import ga.classi.commons.constant.RequestDataConstants;
import ga.classi.commons.constant.StringConstants;

import static ga.classi.api.constant.RequestMappingConstants.PING;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import static ga.classi.api.constant.RequestMappingConstants.DATA_EXCEPTION;
import ga.classi.commons.data.error.DataException;
import ga.classi.commons.data.error.Errors;

@RestController
@Slf4j
public class GeneralController extends BaseController {

    @RequestMapping(PING) // Can be accessed with any HTTP method
    public ResponseObject ping() {
        return ResponseObject.success();
    }

    @GetMapping(DATA_EXCEPTION)
    public ResponseObject dataException(String code) {
        log.info("Data exception: {}", code);
        throw new DataException(Errors.byCode(code)); 
    }
    
}
