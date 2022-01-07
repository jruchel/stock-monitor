package com.jruchel.stockmonitor.services.stocks;

import com.jruchel.stockmonitor.config.GeneralProperties;
import com.jruchel.stockmonitor.models.StockData;
import com.jruchel.stockmonitor.models.entities.MonitoredStock;
import com.jruchel.stockmonitor.models.entities.User;
import com.jruchel.stockmonitor.services.users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledStockCheckService {

    private final GeneralProperties properties;
    private final StockDataService stockDataService;
    private final RestTemplate restTemplate;
    private final StockInformationProcessor stockInformationProcessor;
    private final MonitoredStockService monitoredStockService;
    private final UserService userService;

    @Scheduled(fixedRateString = "#{generalProperties.timeout}")
    public void checkStocks() {
        for (User user : userService.getAllUsers()) {
            checkStocks(user);
        }
    }

    private void checkStocks(User user) {
        List<MonitoredStock> monitoredStockList = monitoredStockService.getAll(user);
        if (monitoredStockList.isEmpty()) return;
        List<StockData> stockData = stockDataService.getMultipleStocksData(monitoredStockList.stream().map(MonitoredStock::getTicker).collect(Collectors.toList()));
        log.info("Current stock prices {}", stockData.get(0).getTimestamp());
        log.info(stockData.toString());

        for (StockData stockData1 : stockData) {
            stockInformationProcessor.processStockData(stockData1);
        }
    }

    @Scheduled(fixedRate = 300000)
    public void wakeup() {
        try {
            restTemplate.getForObject(properties.getWakeupUrl(), String.class);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

}
