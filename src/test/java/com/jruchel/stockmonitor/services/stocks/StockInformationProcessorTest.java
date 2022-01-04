package com.jruchel.stockmonitor.services.stocks;

import com.jruchel.stockmonitor.models.StockData;
import com.jruchel.stockmonitor.models.entities.MonitoredStock;
import com.jruchel.stockmonitor.models.entities.NotificationEvent;
import com.jruchel.stockmonitor.services.notifications.NotificationEventService;
import com.jruchel.stockmonitor.services.notifications.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Date;
import java.util.List;

class StockInformationProcessorTest {

    private List<MonitoredStock> stockList = List.of(new MonitoredStock("1234", "TEST", 500, 550, 5));
    @Mock
    private NotificationEventService notificationEventService;
    @Mock
    private NotificationService notificationService;
    private StockInformationProcessor systemUnderTest;

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.openMocks(this);

        systemUnderTest = new StockInformationProcessor(notificationService, notificationEventService, stockList);
    }

    @Test
    void assertNotificationsAreSentCorrectlyWhenTopValueReached() {
        Mockito.when(notificationEventService.findByTicker("TEST")).thenReturn(new NotificationEvent("12345", "TEST", new Date(), 500, new Date()));

        systemUnderTest.processStockData(new StockData("TEST", 555, new Date().toString()));
        Mockito.verify(notificationService, Mockito.atMostOnce()).sendNotification("TEST", 500, 555);
    }

    @Test
    void assertNotificationsAreSentCorrectlyWhenBottomValueReached() {
        Mockito.when(notificationEventService.findByTicker("TEST")).thenReturn(new NotificationEvent("12345", "TEST", new Date(), 555, new Date()));

        systemUnderTest.processStockData(new StockData("TEST", 500, new Date().toString()));
        Mockito.verify(notificationService, Mockito.atLeastOnce()).sendNotification("TEST", 555, 500);
    }

    @Test
    void assertNotificationsAreSentCorrectlyWhenPercentageMovedUp() {
        Mockito.when(notificationEventService.findByTicker("TEST")).thenReturn(new NotificationEvent("12345", "TEST", new Date(), 600, new Date()));

        systemUnderTest.processStockData(new StockData("TEST", 1000, new Date().toString()));
        Mockito.verify(notificationService, Mockito.atMostOnce()).sendNotification("TEST", 600, 1000);
    }

    @Test
    void assertNotificationsAreSentCorrectlyWhenPercentageMovedDown() {
        Mockito.when(notificationEventService.findByTicker("TEST")).thenReturn(new NotificationEvent("12345", "TEST", new Date(), 450, new Date()));

        systemUnderTest.processStockData(new StockData("TEST", 100, new Date().toString()));
        Mockito.verify(notificationService, Mockito.atLeastOnce()).sendNotification("TEST", 450, 100);
    }

}