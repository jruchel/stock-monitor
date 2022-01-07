package com.jruchel.stockmonitor.services.notifications;

import com.jruchel.stockmonitor.models.entities.NotificationEvent;
import com.jruchel.stockmonitor.models.entities.User;
import com.jruchel.stockmonitor.services.mail.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final MailService mailService;
    private final NotificationEventService notificationEventService;

    public void sendDummyNotification(String ticker, double price) {
        NotificationEvent notificationEvent = NotificationEvent.builder()
                .dateNotified(new Date())
                .priceNotified(price)
                .ticker(ticker)
                .date(new Date())
                .build();

        notificationEventService.saveOrUpdateNotification(notificationEvent);
    }

    public void sendNotification(User user, String ticker, double lastNotifiedPrice, double newPrice) {
        log.info("Sending price change notification for %s from %f to %f".formatted(ticker, lastNotifiedPrice, newPrice));
        mailService.sendCheckpointNotification(user.getEmail(), ticker, newPrice, newPrice >= lastNotifiedPrice);
        notificationEventService.saveOrUpdateNotification(
                NotificationEvent.builder()
                        .dateNotified(new Date())
                        .ticker(ticker)
                        .priceNotified(newPrice)
                        .build()
        );
    }

}
