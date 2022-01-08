package com.jruchel.stockmonitor.services.notifications;

import com.jruchel.stockmonitor.models.entities.MonitoredStock;
import com.jruchel.stockmonitor.models.entities.NotificationEvent;
import com.jruchel.stockmonitor.repositories.NotificationEventRepository;
import com.jruchel.stockmonitor.services.stocks.MonitoredStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationEventService {

    private final NotificationEventRepository repository;
    private final MonitoredStockService monitoredStockService;

    public NotificationEvent saveOrUpdateNotification(NotificationEvent notificationEvent, MonitoredStock monitoredStock) {
        NotificationEvent lastNotification = monitoredStock.getLastNotification();
        if (lastNotification != null) {
            notificationEvent.setId(lastNotification.getId());
        }
        monitoredStock.setLastNotification(notificationEvent);
        monitoredStock = monitoredStockService.save(monitoredStock);
        return monitoredStock.getLastNotification();
    }

    public List<NotificationEvent> getAll() {
        return repository.findAll();
    }

}
