package com.jruchel.stockmonitor.controllers.mappers;

import com.jruchel.stockmonitor.controllers.models.MonitoredStockResponse;
import com.jruchel.stockmonitor.controllers.models.StockMonitoringRequest;
import com.jruchel.stockmonitor.controllers.models.StockMonitoringUpdateRequest;
import com.jruchel.stockmonitor.models.entities.MonitoredStock;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StockMonitoringRequestToStockMapper {

    public MonitoredStock monitoredStock(StockMonitoringUpdateRequest request, String ticker) {
        return MonitoredStock.builder()
                .id(request.getId())
                .ticker(ticker)
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

    public MonitoredStockResponse monitoredStockResponse(MonitoredStock stock) {
        return MonitoredStockResponse.builder()
                .id(stock.getId())
                .ticker(stock.getTicker())
                .notifyAbove(stock.getNotifyAbove())
                .notifyBelow(stock.getNotifyBelow())
                .notifyEveryPercent(stock.getNotifyEveryPercent()).build();
    }

    public List<MonitoredStockResponse> monitoredStockResponseList(List<MonitoredStock> monitoredStockList) {
        return monitoredStockList.stream().map(this::monitoredStockResponse).collect(Collectors.toList());
    }
}
