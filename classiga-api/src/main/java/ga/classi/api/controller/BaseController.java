/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.api.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.LocaleResolver;

import ga.classi.commons.data.error.DataException;
import ga.classi.commons.constant.CommonConstants;
import ga.classi.commons.utility.MessageHelper;
import ga.classi.api.helper.ResponseObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseController {

    @Autowired
    protected LocaleResolver localeResolver;
    
    @Autowired
    protected MessageHelper messageHelper;

    @Autowired
    protected HttpServletRequest request;

    protected String createResponseMessage(String code, Object[] arguments) {
        Locale locale = localeResolver.resolveLocale(request);
        if (locale != null) {
            return messageHelper.getMessage(code, arguments, locale);
        }
        return messageHelper.getMessage(code, arguments);
    }

    protected String getResponseMessage(String code) {
        return createResponseMessage(code, null);
    }

    @ExceptionHandler(DataException.class)
    public ResponseObject handleDataException(DataException e) {
        log.error("Data exception caught! {}", e.toString());
        return new ResponseObject(CommonConstants.FAIL, createResponseMessage(e.getMessage(), e.getData()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseObject handleOtherException(Exception e) {
        log.error("Other exception caught!", e);
        return new ResponseObject(CommonConstants.ERROR, getResponseMessage("error.ServerError"));
    }

}
