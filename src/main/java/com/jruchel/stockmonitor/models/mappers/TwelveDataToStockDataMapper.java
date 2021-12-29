package com.jruchel.stockmonitor.models.mappers;

import com.jruchel.stockmonitor.models.StockData;
import com.jruchel.stockmonitor.models.ato.twelvedata.TwelveDataResponse;
import com.jruchel.stockmonitor.models.ato.twelvedata.TwelveDataStockDataATO;
import com.jruchel.stockmonitor.models.ato.twelvedata.Value;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TwelveDataToStockDataMapper {

    public List<StockData> map(TwelveDataResponse twelveData) {
        List<StockData> stockData = new ArrayList<>();
        for (String key : twelveData.getData().keySet()) {
            TwelveDataStockDataATO data = twelveData.getData().get(key);
            stockData.add(map(data));
        }
        return stockData;
    }

    public StockData map(TwelveDataStockDataATO twelveData) {
        StockData stockData = new StockData();

        Value inspectedValue = twelveData.getValues().get(0);

        stockData.setTicker(twelveData.getMeta().getSymbol());
        stockData.setPrice(inspectedValue.getClosingPrice());
        stockData.setTimestamp(new Date().toString());

        return stockData;
    }

}
