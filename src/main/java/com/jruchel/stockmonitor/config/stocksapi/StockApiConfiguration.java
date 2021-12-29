package com.jruchel.stockmonitor.config.stocksapi;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties("stock-api")
public class StockApiConfiguration {
    private String address;
    private String mainEndpoint;
    private Key key;
    private List<String> stocks;

    @Getter
    @Setter
    public static class Key {
        private String name;
        private String value;
    }

}
