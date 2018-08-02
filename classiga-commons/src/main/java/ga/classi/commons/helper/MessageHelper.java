/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.commons.helper;

import java.util.Locale;

import org.springframework.context.MessageSource;

public class MessageHelper {

    private MessageSource messageSource;
    private Locale defaultLocale;

    public String getMessage(String code, Object[] args, Locale locale) {
        if (locale == null) {
            locale = Locale.ENGLISH;
        }
        return messageSource.getMessage(code, args, code, locale);
    }

    public String getMessage(String code, Object[] args) {
        return getMessage(code, args, defaultLocale);
    }

    public String getMessage(String code) {
        return getMessage(code, null);
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setDefaultLocale(Locale locale) {
        this.defaultLocale = locale;
    }

    public Locale getDefaultLocale() {
        return defaultLocale;
    }

}
