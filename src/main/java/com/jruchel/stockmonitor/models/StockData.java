package com.jruchel.stockmonitor.models;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockData {

    private String ticker;
    private String price;
    private String timestamp;

    @Override
    public String toString() {
        return String.format("%s: %s", ticker, price);
    }
}
