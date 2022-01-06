package com.jruchel.stockmonitor.config;

import com.jruchel.stockmonitor.models.entities.MonitoredStock;
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
    private List<MonitoredStock> stocks;
    private long timeout;
    private String wakeupUrl;
    private Admin admin;

    @Bean(name = "listOfStocks")
    public List<MonitoredStock> getStocks() {
        return stocks;
    }

    @Getter
    @Setter
    public static class Admin {
        private String username;
        private String password;
        private String email;
    }

}
