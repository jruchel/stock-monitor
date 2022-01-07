package com.jruchel.stockmonitor.services.stocks;

import com.jruchel.stockmonitor.models.StockData;
import com.jruchel.stockmonitor.models.entities.MonitoredStock;
import com.jruchel.stockmonitor.models.entities.NotificationEvent;
import com.jruchel.stockmonitor.models.entities.User;
import com.jruchel.stockmonitor.services.notifications.NotificationEventService;
import com.jruchel.stockmonitor.services.notifications.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockInformationProcessor {

    private final NotificationService notificationService;
    private final MonitoredStockService monitoredStockService;
    @Qualifier("listOfStocks")
    private final List<MonitoredStock> stockList;

    public void processStockData(StockData stockData) {
        List<User> interestedUsers = monitoredStockService.getUsersMonitoringStock(stockData.getTicker());

        for (User user : interestedUsers) {
            List<MonitoredStock> associatedStocks = getAssociatedStocks(user, stockData.getTicker());

            for (MonitoredStock stock : associatedStocks) {
                if (stock.getLastNotification() == null) {
                    notificationService.sendDummyNotification(stock, stockData.getPrice());
                } else if (shouldNotify(stockData, stock)) {
                    notificationService.sendNotification(stock, stock.getLastNotification().getPriceNotified(), stockData.getPrice());
                }
            }
        }
    }


    private List<MonitoredStock> getAssociatedStocks(User user, String ticker) {
        return user.getMonitoredStocks().stream().filter(stock -> stock.getTicker().equals(ticker)).collect(Collectors.toList());
    }

    private boolean shouldNotify(StockData stockData, MonitoredStock relevantStock) {
        NotificationEvent previousNotification = relevantStock.getLastNotification();
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
        return Math.abs((price1 - price2) / price1 * 100);
    }
}
