package com.jruchel.stockmonitor.services.notifications;

import com.jruchel.stockmonitor.models.entities.MonitoredStock;
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

    public void sendDummyNotification(MonitoredStock monitoredStock, double price) {
        NotificationEvent notificationEvent = NotificationEvent.builder()
                .dateNotified(new Date())
                .priceNotified(price)
                .ticker(monitoredStock.getTicker())
                .date(new Date())
                .build();

        notificationEventService.saveOrUpdateNotification(notificationEvent, monitoredStock);
    }

    public void sendNotification(MonitoredStock monitoredStock, double lastNotifiedPrice, double newPrice) {
        User user = monitoredStock.getUser();
        log.info("Sending price change notification for %s from %f to %f".formatted(monitoredStock.getTicker(), lastNotifiedPrice, newPrice));
        mailService.sendCheckpointNotification(user.getEmail(), monitoredStock.getTicker(), newPrice, newPrice >= lastNotifiedPrice);
        notificationEventService.saveOrUpdateNotification(
                NotificationEvent.builder()
                        .dateNotified(new Date())
                        .ticker(monitoredStock.getTicker())
                        .priceNotified(newPrice)
                        .build(), monitoredStock
        );
    }

}
