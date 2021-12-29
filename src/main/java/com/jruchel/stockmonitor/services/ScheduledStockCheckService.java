package com.jruchel.stockmonitor.services;

import com.jruchel.stockmonitor.aspects.BreakCircuit;
import com.jruchel.stockmonitor.config.scheduling.GeneralProperties;
import com.jruchel.stockmonitor.models.StockData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledStockCheckService {

    private final GeneralProperties properties;
    private final StockDataService stockDataService;

    @BreakCircuit
    @Scheduled(fixedRateString = "#{generalProperties.timeout}")
    public void checkStocks() {
        List<StockData> stockData = stockDataService.getMultipleStocksData(properties.getStocks());
        log.info("Current stock prices {}", stockData.get(0).getTimestamp());
        log.info(stockData.toString());
    }

}
