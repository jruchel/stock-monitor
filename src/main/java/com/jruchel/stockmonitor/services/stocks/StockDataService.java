package com.jruchel.stockmonitor.services.stocks;

import com.jruchel.stockmonitor.models.StockData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockDataService {

    private final StockDataRepository repository;

    public StockData getSingleStockData(String ticker) {
        return repository.fetchStockData(ticker);
    }

    public List<StockData> getMultipleStocksData(List<String> tickers) {
        return repository.fetchStockData(tickers);
    }

}
