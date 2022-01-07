package com.jruchel.stockmonitor.services.stocks;

import com.jruchel.stockmonitor.StockMonitorApplication;
import com.jruchel.stockmonitor.controllers.models.StockMonitoringUpdateRequest;
import com.jruchel.stockmonitor.models.entities.MonitoredStock;
import com.jruchel.stockmonitor.models.entities.Role;
import com.jruchel.stockmonitor.models.entities.User;
import com.jruchel.stockmonitor.repositories.MonitoredStockRepository;
import com.jruchel.stockmonitor.repositories.RoleRepository;
import com.jruchel.stockmonitor.repositories.UserRepository;
import com.jruchel.stockmonitor.security.SecurityService;
import com.jruchel.stockmonitor.services.StartupService;
import com.jruchel.stockmonitor.services.users.RoleService;
import com.jruchel.stockmonitor.services.users.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StockMonitorApplication.class)
class MonitoredStockServiceTest {

    @Autowired
    private MonitoredStockService systemUnderTest;

    @Autowired
    private MonitoredStockRepository monitoredStockRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;

    @MockBean
    private SecurityService securityService;

    @MockBean
    private StartupService startupService;

    private User user;

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        Role userRole = roleService.createUserRole();
        user = userService.save(getTestUser(userRole));
        Mockito.when(securityService.getCurrentUser()).thenReturn(user);
    }

    private User getTestUser(Role role) {
        return User.builder().monitoredStocks(new ArrayList<>()).username("test").password("test").email("test@test.com").roles(Set.of(role)).build();
    }

    @Test
    void saveAndRetrieveStockForUser() {
        MonitoredStock stock = MonitoredStock.builder().notifyAbove(100).notifyAbove(120).notifyEveryPercent(5).ticker("TEST").build();
        systemUnderTest.saveStockForUser(stock, user);
        MonitoredStock resultStock = systemUnderTest.getStockByTicker("TEST", user);

        assertNotNull(resultStock);
        assertTrue(stockSameAs(stock, resultStock));
    }

    @Test
    void updateStockForUser() {
        MonitoredStock stock = MonitoredStock.builder().notifyAbove(100).notifyAbove(120).notifyEveryPercent(5).ticker("TEST").build();
        systemUnderTest.saveStockForUser(stock, user);
        MonitoredStock savedStock = systemUnderTest.getStockByTicker("TEST", user);

        assertNotNull(savedStock);
        assertTrue(stockSameAs(stock, savedStock));

        systemUnderTest.update(new StockMonitoringUpdateRequest(savedStock.getId(), 50, 50, 50), user);

        MonitoredStock resultStock = systemUnderTest.getStockByTicker("TEST", user);

        assertNotNull(resultStock);
        assertEquals(50, resultStock.getNotifyAbove());
        assertEquals(50, resultStock.getNotifyAbove());
        assertEquals(50, resultStock.getNotifyEveryPercent());
    }

    @Test
    void deleteStockForUser() {
        MonitoredStock stock = MonitoredStock.builder().notifyAbove(100).notifyAbove(120).notifyEveryPercent(5).ticker("TEST").build();
        systemUnderTest.saveStockForUser(stock, user);
        MonitoredStock savedStock = systemUnderTest.getStockByTicker("TEST", user);

        assertNotNull(savedStock);
        assertTrue(stockSameAs(stock, savedStock));

        systemUnderTest.delete(savedStock.getId(), user);

        MonitoredStock resultStock = systemUnderTest.getStockByTicker("TEST", user);

        assertNull(resultStock);
        assertNotNull(userService.getByUsername(user.getUsername()));
    }

    private boolean stockSameAs(MonitoredStock stock, MonitoredStock stock2) {
        return stock.getNotifyBelow() == stock2.getNotifyBelow() && stock.getTicker().equals(stock2.getTicker()) && stock.getNotifyAbove() == stock2.getNotifyAbove() && stock.getNotifyEveryPercent() == stock2.getNotifyEveryPercent();
    }

}