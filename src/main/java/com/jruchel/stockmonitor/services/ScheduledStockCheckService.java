package com.jruchel.stockmonitor.services;

import com.jruchel.stockmonitor.aspects.BreakCircuit;
import com.jruchel.stockmonitor.config.GeneralProperties;
import com.jruchel.stockmonitor.models.Stock;
import com.jruchel.stockmonitor.models.StockData;
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
    private final RestTemplate restTemplate = new RestTemplate();
    private final StockInformationProcessor stockInformationProcessor;

    @BreakCircuit
    @Scheduled(fixedRateString = "#{generalProperties.timeout}")
    public void checkStocks() {
        List<StockData> stockData = stockDataService.getMultipleStocksData(properties.getStocks().stream().map(Stock::getTicker).collect(Collectors.toList()));
        log.info("Current stock prices {}", stockData.get(0).getTimestamp());
        log.info(stockData.toString());

        for (StockData stockData1 : stockData) {
            stockInformationProcessor.processStockData(stockData1);
        }
    }

    public void wakeup() {
        restTemplate.getForObject(properties.getWakeupUrl(), String.class);
    }

}
