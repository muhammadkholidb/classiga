/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.commons.web.utility;

import java.io.IOException;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 *
 * @author muhammad
 */
@Slf4j
@Setter
@Getter
public class EmailServiceBean {
    
    private JavaMailSender mailSender;

    public String getRemoteContent(String url, Map<String, ?> body, HttpMethod method) {
        try {
            HTTP http = new HTTP();
            http.setUrl(url);
            http.setBody(body);
            HTTPResponse result = http.request(method);
            return result.getResponse(); // HTML
        } catch (IOException ex) {
            log.error("Failed to get email content", ex);
        }
        return "";
    }
 
    public void sendEmail(String from, String to, String subject, String message) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(message, true);
        mailSender.send(mimeMessage);
    }
}
