package com.jruchel.stockmonitor.services.stockapi;

import com.jruchel.stockmonitor.config.stocksapi.StockApiConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Stream;

class YahooFinanceWebscrapeServiceTest {

    private YahooFinanceWebscrapeService systemUnderTest;
    @Mock
    private StockApiConfiguration configuration;

    @BeforeEach
    private void beforeEach() {
        MockitoAnnotations.openMocks(this);
        systemUnderTest = new YahooFinanceWebscrapeService(configuration, new RestTemplate());
    }

    @ParameterizedTest
    @MethodSource("getTickers")
    void test(String ticker) {
        double[] result = new double[1];
        Assertions.assertDoesNotThrow(() -> {
            result[0] = systemUnderTest.getStockPriceForTicker(ticker);
        });
        Assertions.assertTrue(result[0] > 0);
    }

    private static Stream<String> getTickers() {
        return Stream.of(
                "TSLA", "DNA", "NVDA", "AMD", "ARKK", "ARKG", "AMZN", "GOOG", "GOOGL", "BRK-B"
        );
    }

}