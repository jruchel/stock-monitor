package com.jruchel.stockmonitor.controllers;

import com.jruchel.stockmonitor.models.StockData;
import com.jruchel.stockmonitor.services.stocks.StockDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stocks/data")
public class StockDataController {

    private final StockDataService stockDataService;

    @GetMapping
    public ResponseEntity<?> getStockData(@RequestParam List<String> ticker) {
        if (ticker.size() == 1) return getSingleStockData(ticker.get(0));
        return getMultipleStockData(ticker);
    }

    public ResponseEntity<StockData> getSingleStockData(String ticker) {
        return ResponseEntity.ok(stockDataService.getSingleStockData(ticker));
    }

    public ResponseEntity<List<StockData>> getMultipleStockData(List<String> tickers) {
        return ResponseEntity.ok(stockDataService.getMultipleStocksData(tickers));
    }


}
