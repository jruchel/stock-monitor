package com.jruchel.stockmonitor.config.stocksapi;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("stock-api")
public class StockApiConfiguration {
    private String address;
    private String mainEndpoint;
    private Key key;

    @Getter
    @Setter
    public static class Key {
        private String name;
        private String value;
    }

}
