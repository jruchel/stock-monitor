package com.jruchel.stockmonitor.services.stocks;

import com.jruchel.stockmonitor.config.stocksapi.StockApiConfiguration;
import com.jruchel.stockmonitor.models.StockData;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class StockDataRepository {

    protected final StockApiConfiguration configuration;

    public abstract StockData fetchStockData(String ticker);

    public List<StockData> fetchStockData(String... tickers) {
        return fetchStockData(Arrays.stream(tickers).toList());
    }

    public List<StockData> fetchStockData(List<String> tickers) {
        return tickers.stream().map(this::fetchStockData).collect(Collectors.toList());
    }

}
