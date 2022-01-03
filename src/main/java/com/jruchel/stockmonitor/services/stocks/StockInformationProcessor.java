package com.jruchel.stockmonitor.services.stocks;

import com.jruchel.stockmonitor.models.entities.MonitoredStock;
import com.jruchel.stockmonitor.models.StockData;
import com.jruchel.stockmonitor.models.entities.NotificationEvent;
import com.jruchel.stockmonitor.services.notifications.NotificationEventService;
import com.jruchel.stockmonitor.services.mail.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockInformationProcessor {

    private final MailService mailService;
    private final NotificationEventService notificationEventService;
    @Qualifier("listOfStocks")
    private final List<MonitoredStock> stockList;

    public void processStockData(StockData stockData) {
        NotificationEvent previousNotification = notificationEventService.findByTicker(stockData.getTicker());
        if (shouldNotify(stockData, previousNotification)) {
            log.info("Sending price change notification for %s from %f to %f".formatted(stockData.getTicker(), previousNotification.getPriceNotified(), stockData.getPrice()));
            mailService.sendCheckpointNotification(stockData.getTicker(), stockData.getPrice(), stockData.getPrice() >= previousNotification.getPriceNotified());
            notificationEventService.saveOrUpdateNotification(
                    NotificationEvent.builder()
                            .dateNotified(new Date())
                            .ticker(stockData.getTicker())
                            .priceNotified(stockData.getPrice())
                            .build()
            );
        }
    }

    private boolean shouldNotify(StockData stockData, NotificationEvent previousNotification) {
        MonitoredStock relevantStock = getRelevantStock(stockData);
        if (relevantStock == null) return false;
        if (previousNotification != null) {
            if (stockData.getPrice() > relevantStock.getNotifyAbove() && previousNotification.getPriceNotified() < relevantStock.getNotifyAbove())
                return true;
            if (stockData.getPrice() < relevantStock.getNotifyBelow() && previousNotification.getPriceNotified() > relevantStock.getNotifyBelow())
                return true;
        } else {
            if (stockData.getPrice() > relevantStock.getNotifyAbove()) return true;
            if (stockData.getPrice() < relevantStock.getNotifyBelow()) return true;
        }
        if (previousNotification == null) return false;
        return getPriceDifferenceInPercentage(previousNotification.getPriceNotified(), stockData.getPrice()) >= relevantStock.getNotifyEveryPercent();
    }

    private MonitoredStock getRelevantStock(StockData stockData) {
        return stockList.stream().filter(stock -> stock.getTicker().equals(stockData.getTicker())).findFirst().orElse(null);
    }


    private double getPriceDifferenceInPercentage(double price1, double price2) {
        return (price1 - price2) / price1 * 100;
    }
}
