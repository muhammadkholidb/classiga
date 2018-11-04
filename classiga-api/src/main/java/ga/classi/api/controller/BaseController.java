/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.api.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.LocaleResolver;

import ga.classi.commons.utility.MessageHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseController {

    @Autowired
    protected LocaleResolver localeResolver;

    @Autowired
    protected MessageHelper messageHelper;

    @Autowired
    protected HttpServletRequest request;

    protected String getResponseMessage(String code, Object[] arguments) {
        Locale locale = localeResolver.resolveLocale(request);
        return messageHelper.getMessage(code, arguments, locale);
    }

    protected String getResponseMessage(String code) {
        return getResponseMessage(code, null);
    }

}
