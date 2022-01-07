package com.jruchel.stockmonitor.services.stocks;

import com.jruchel.stockmonitor.models.entities.MonitoredStock;
import com.jruchel.stockmonitor.services.notifications.NotificationEventService;
import com.jruchel.stockmonitor.services.notifications.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

class StockInformationProcessorTest {

    private List<MonitoredStock> stockList;
    @Mock
    private NotificationEventService notificationEventService;
    @Mock
    private NotificationService notificationService;
    @Mock
    private MonitoredStockService monitoredStockService;
    private StockInformationProcessor systemUnderTest;

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.openMocks(this);

        systemUnderTest = new StockInformationProcessor(notificationService, monitoredStockService, stockList);
    }

    @Test
    void assertNotificationsAreSentCorrectlyWhenTopValueReached() {

    }

    @Test
    void assertNotificationsAreSentCorrectlyWhenBottomValueReached() {

    }

    @Test
    void assertNotificationsAreSentCorrectlyWhenPercentageMovedUp() {

    }

    @Test
    void assertNotificationsAreSentCorrectlyWhenPercentageMovedDown() {

    }

}