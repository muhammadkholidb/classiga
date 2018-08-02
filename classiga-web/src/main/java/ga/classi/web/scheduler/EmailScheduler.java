/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.web.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailScheduler {

    @Autowired
    private MailSender mailSender;

    @Value("${mail.from}")
    private String from;
    
    // @Scheduled(fixedRate = 30000)
    public void sendEmail() {
        log.info("Send email ...");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo("eatonmunoz@yopmail.com");
        message.setSubject("Test Spring Mail");
        message.setText("Test Spring Mail");
        mailSender.send(message);
    }

}
