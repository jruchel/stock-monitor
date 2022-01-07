package com.jruchel.stockmonitor.services.stocks;

import com.jruchel.stockmonitor.config.GeneralProperties;
import com.jruchel.stockmonitor.config.security.JWTConfig;
import com.jruchel.stockmonitor.models.StockData;
import com.jruchel.stockmonitor.models.entities.MonitoredStock;
import com.jruchel.stockmonitor.models.entities.User;
import com.jruchel.stockmonitor.security.jwt.JWTUtils;
import com.jruchel.stockmonitor.services.users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledStockCheckService {

    private final GeneralProperties properties;
    private final StockDataService stockDataService;
    private final RestTemplate restTemplate;
    private final StockInformationProcessor stockInformationProcessor;
    private final MonitoredStockService monitoredStockService;
    private final UserService userService;
    private final JWTConfig jwtConfig;
    private final JWTUtils jwtUtils;

    @Scheduled(fixedRateString = "#{generalProperties.timeout}")
    public void checkStocks() {
        for (User user : userService.getAllUsers()) {
            checkStocks(user);
        }
    }

    private void checkStocks(User user) {
        List<MonitoredStock> monitoredStockList = monitoredStockService.getAll(user);
        if (monitoredStockList.isEmpty()) return;
        List<StockData> stockData = stockDataService.getMultipleStocksData(monitoredStockList.stream().map(MonitoredStock::getTicker).collect(Collectors.toList()));
        log.info("Current stock prices {}", stockData.get(0).getTimestamp());
        log.info(stockData.toString());

        for (StockData stockData1 : stockData) {
            stockInformationProcessor.processStockData(stockData1);
        }
    }

    @Scheduled(fixedRate = 10000)
    public void wakeup() {
        try {
            RequestEntity<Void> requestEntity = RequestEntity
                    .get(
                            URI.create(properties.getWakeupUrl())
                    )
                    .header(
                            jwtConfig.getJwtHeaderName(),
                            jwtUtils.generateToken(userService.getAdminUser())
                    ).build();

            restTemplate.exchange(requestEntity, String.class);
            log.info("Keeping the app awake");
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

}
