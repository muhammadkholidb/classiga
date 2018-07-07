package ga.classi.web.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailScheduler {

    @Autowired
    private MailSender mailSender;

    @Scheduled(fixedRate = 30000)
    public void sendEmail() {
        log.info("Send email ...");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("Nasi Goreng Biru<nasigorengbiru@gmail.com>");
        message.setTo("eatonmunoz@yopmail.com");
        message.setSubject("Test Spring Mail");
        message.setText("Test Spring Mail");
        mailSender.send(message);
    }

}
