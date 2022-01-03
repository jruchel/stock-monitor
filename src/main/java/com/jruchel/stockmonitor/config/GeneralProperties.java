package com.jruchel.stockmonitor.config;

import com.jruchel.stockmonitor.models.Stock;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@ConfigurationProperties("general")
@Getter
@Setter
public class GeneralProperties {
    private String notificationEmail;
    private List<Stock> stocks;
    private long timeout;
    private String wakeupUrl;

    @Bean(name = "listOfStocks")
    public List<Stock> getStocks() {
        return stocks;
    }
}
