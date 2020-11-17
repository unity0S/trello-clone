package io.molnarsandor.trelloclone.util;

import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailService {
    private final Log log = LogFactory.getLog(this.getClass());

    @Value("${spring.mail.username}")
    private String messageFrom;

    private final JavaMailSender javaMailSender;

    public void sendMessage(String email, String subject, String text) {
        SimpleMailMessage message;

        try {
            message = new SimpleMailMessage();
            message.setFrom(messageFrom);
            message.setTo(email);
            message.setSubject(subject);
            message.setText(text);
            javaMailSender.send(message);
            log.info("Mail sent to: " + email);
        } catch (MailException e) {
            log.error("Error sending mail to: " + email + " Error: " + e);
        }
    }

}
