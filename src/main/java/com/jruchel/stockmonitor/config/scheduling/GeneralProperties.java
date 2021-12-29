package com.jruchel.stockmonitor.config.scheduling;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties("general")
@Getter
@Setter
public class GeneralProperties {
    private List<String> stocks;
    private long timeout;
}
