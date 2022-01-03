package com.jruchel.stockmonitor.services;

import com.jruchel.stockmonitor.config.GeneralProperties;
import com.jruchel.stockmonitor.services.stocks.MonitoredStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StartupService {

    private final GeneralProperties properties;
    private final MonitoredStockService monitoredStockService;

    @EventListener(ApplicationReadyEvent.class)
    public void startup() {
        monitoredStockService.addStockMonitoring(properties.getStocks());
    }

}
