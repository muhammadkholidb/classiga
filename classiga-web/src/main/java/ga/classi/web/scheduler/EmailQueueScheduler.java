/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.web.scheduler;

import ga.classi.commons.constant.StringConstants;
import ga.classi.commons.data.constant.QueueStatus;
import ga.classi.commons.utility.ActionResult;
import ga.classi.commons.utility.CommonUtils;
import ga.classi.commons.utility.StringCheck;
import ga.classi.commons.web.utility.HTTP;
import ga.classi.commons.web.utility.HTTPResponse;
import ga.classi.commons.web.utility.JSON;
import ga.classi.web.controller.base.BaseControllerAdapter;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailQueueScheduler extends BaseControllerAdapter {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.from}")
    private String from;

    @Value("${app.url.host}")
    private String appUrl;
    
    @Scheduled(fixedRateString = "${mail.scheduler.interval.millisecond}")
    public void execute() {
        log.info("Execute ...");
        getPendingQueues().thenAccept(this::processEmailQueues);
    }

    private CompletableFuture<JSONArray> getPendingQueues() {
        CompletableFuture<JSONArray> future = CompletableFuture.supplyAsync(() -> {
            ActionResult res = getEmailQueuesByStatus(QueueStatus.PENDING.id());
            if (!res.isSuccess()) {
                return new JSONArray();
            }
            return (JSONArray) res.getContent();
        });
        return future;
    }

    private void processQueue(JSONObject queue) throws MessagingException {
        String data = (String) queue.get("data");
        String template = (String) queue.get("template");
        String message = (String) queue.get("message");
        String to = (String) queue.get("to");
        String subject = (String) queue.get("subject");
        JSONObject dataJson = JSON.parse(data, JSONObject.class);
        if (dataJson != null) {
            if (dataJson.containsKey("fullName")) {
                to = dataJson.get("fullName") + "<" + to + ">";
            } else if (dataJson.containsKey("username")) {
                to = dataJson.get("username") + "<" + to + ">";
            }
        }
        String text = getEmailContent(template, message, dataJson);
        sendEmail(to, subject, text);
    }

    private void sendEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);
        mailSender.send(mimeMessage);
    }
    
    private String getEmailContent(String template, String message, JSONObject data) {
        String content = "";
        // Check for template first
        if (!StringCheck.isEmpty(template)) {
            content = requestEmailContent(template, (data == null) ? StringConstants.EMPTY_JSON : JSON.stringify(data));
        } else {
            // If template is empty, message must not be empty or null
            if (data == null) {
                content = message;
            } else {
                for (Object key : data.keySet()) {
                    content = message.replaceAll(StringConstants.CURLY_BRACKET_OPEN + key + StringConstants.CURLY_BRACKET_CLOSE, data.get(key).toString());
                }
            }
        }
        return content;
    }

    private String requestEmailContent(String template, String data) {
        try {
            HTTP http = new HTTP();
            http.setUrl(appUrl + "/generate-html");
            http.setBody(CommonUtils.map("template", template, "data", data));
            HTTPResponse result = http.post();
            return result.getResponse(); // HTML
        } catch (IOException ex) {
            log.error("Failed to get email content", ex);
        }
        return "";
    }
    
    private void processEmailQueues(JSONArray queues) {
        // Loop through the queues
        // Update queue status to PROCESSING
        // Send email
        // Update queue status to DONE
        queues.forEach((o) -> {
            JSONObject queue = (JSONObject) o;
            CompletableFuture.supplyAsync(() -> {
                queue.put("status", QueueStatus.PROCESSING.id());
                ActionResult res = editEmailQueue(queue);
                if (res.isSuccess()) {
                    return (JSONObject) res.getContent();
                }
                return null;
            }).thenApply((processingQueue) -> {
                if (processingQueue == null) {
                    return null;
                }
                try {
                    processQueue(processingQueue);
                } catch (MessagingException ex) {
                    throw new CompletionException(ex);
                }
                return processingQueue;
            }).handle((processingQueue, ex) -> {
                if (ex != null) {
                    log.error("Failed to send email", ex);
                }
                return processingQueue;
            }).thenAccept((processedQueue) -> {
                // processedQueue should not be null
                processedQueue.put("status", QueueStatus.DONE.id());
                ActionResult res = editEmailQueue(processedQueue);
                log.debug("Update to DONE: {}", res.getStatus());
                if (!res.isSuccess()) {
                    log.debug("Message: {}", res.getMessage());
                }
            });
        });
    }

}
