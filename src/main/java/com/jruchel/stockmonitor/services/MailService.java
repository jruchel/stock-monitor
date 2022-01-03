package com.jruchel.stockmonitor.services;

import com.jruchel.stockmonitor.config.GeneralProperties;
import com.jruchel.stockmonitor.config.mail.MailConfig;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Getter
@Setter
@Slf4j
@RequiredArgsConstructor
public class MailService {

    private final MailConfig mailConfig;
    private final JavaMailSender mailSender;
    private final GeneralProperties generalProperties;

    @Async
    public void sendMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("stockupdate@stockmonitor.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);

        try {
            mailSender.send(message);
            log.info("Stock data notification sent.");
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    @Async
    public void sendCheckpointNotification(String ticker, double newPrice, boolean goneUp) {
        sendMail(generalProperties.getNotificationEmail(), "%s price has changed".formatted(ticker), String.format("%s stock has gone %s to %f - %s", ticker, goneUp ? "up" : "down", newPrice, new Date().toString()));
    }

}
