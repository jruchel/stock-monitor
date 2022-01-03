package com.jruchel.stockmonitor.services;

import com.jruchel.stockmonitor.models.entities.NotificationEvent;
import com.jruchel.stockmonitor.repositories.NotificationEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationEventService {

    private final NotificationEventRepository repository;

    public NotificationEvent saveOrUpdateNotification(NotificationEvent notificationEvent) {
        NotificationEvent previousEvent = findByTicker(notificationEvent.getTicker());
        if (previousEvent != null) repository.deleteByTicker(notificationEvent.getTicker());
        return repository.save(notificationEvent);
    }

    public NotificationEvent findByTicker(String ticker) {
        return repository.findByTicker(ticker);
    }

}
