package com.jruchel.stockmonitor.services.stocks;

import com.jruchel.stockmonitor.models.StockData;
import com.jruchel.stockmonitor.models.entities.MonitoredStock;
import com.jruchel.stockmonitor.models.entities.NotificationEvent;
import com.jruchel.stockmonitor.services.notifications.NotificationEventService;
import com.jruchel.stockmonitor.services.notifications.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockInformationProcessor {

    private final NotificationService notificationService;
    private final NotificationEventService notificationEventService;
    @Qualifier("listOfStocks")
    private final List<MonitoredStock> stockList;

    public void processStockData(StockData stockData) {
        NotificationEvent previousNotification = notificationEventService.findByTicker(stockData.getTicker());
        if (previousNotification == null)
            notificationService.sendDummyNotification(stockData.getTicker(), stockData.getPrice());
        else if (shouldNotify(stockData, previousNotification))
            notificationService.sendNotification(stockData.getTicker(), previousNotification.getPriceNotified(), stockData.getPrice());

    }

    private boolean shouldNotify(StockData stockData, NotificationEvent previousNotification) {
        MonitoredStock relevantStock = getRelevantStock(stockData);
        if (relevantStock == null) return false;
        if (previousNotification != null) {
            if (relevantStock.getNotifyAbove() > 0 && stockData.getPrice() > relevantStock.getNotifyAbove() && previousNotification.getPriceNotified() < relevantStock.getNotifyAbove())
                return true;
            if (relevantStock.getNotifyBelow() > 0 && stockData.getPrice() < relevantStock.getNotifyBelow() && previousNotification.getPriceNotified() > relevantStock.getNotifyBelow())
                return true;
        } else {
            if (relevantStock.getNotifyAbove() > 0 && stockData.getPrice() > relevantStock.getNotifyAbove())
                return true;
            if (relevantStock.getNotifyBelow() > 0 && stockData.getPrice() < relevantStock.getNotifyBelow())
                return true;
        }
        if (previousNotification == null || relevantStock.getNotifyEveryPercent() <= 0) return false;
        return getPriceDifferenceInPercentage(previousNotification.getPriceNotified(), stockData.getPrice()) >= relevantStock.getNotifyEveryPercent();
    }

    private MonitoredStock getRelevantStock(StockData stockData) {
        return stockList.stream().filter(stock -> stock.getTicker().equals(stockData.getTicker())).findFirst().orElse(null);
    }


    private double getPriceDifferenceInPercentage(double price1, double price2) {
        return (price1 - price2) / price1 * 100;
    }
}
