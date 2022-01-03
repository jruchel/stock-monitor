package com.jruchel.stockmonitor.controllers.mappers;

import com.jruchel.stockmonitor.controllers.models.StockMonitoringRequest;
import com.jruchel.stockmonitor.controllers.models.StockMonitoringUpdateRequest;
import com.jruchel.stockmonitor.models.entities.MonitoredStock;
import org.springframework.stereotype.Component;

@Component
public class StockMonitoringRequestToStockMapper {

    public MonitoredStock monitoredStock(StockMonitoringUpdateRequest request) {
        return MonitoredStock.builder()
                .id(request.getId())
                .ticker(request.getTicker())
                .notifyAbove(request.getNotifyAbove())
                .notifyBelow(request.getNotifyBelow())
                .notifyEveryPercent(request.getNotifyEveryPercent())
                .build();
    }

    public MonitoredStock monitoredStock(StockMonitoringRequest request) {
        return MonitoredStock.builder()
                .ticker(request.getTicker())
                .notifyAbove(request.getNotifyAbove())
                .notifyBelow(request.getNotifyBelow())
                .notifyEveryPercent(request.getNotifyEveryPercent())
                .build();
    }
}
