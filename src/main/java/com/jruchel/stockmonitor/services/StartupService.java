package com.jruchel.stockmonitor.services;

import com.jruchel.stockmonitor.config.GeneralProperties;
import com.jruchel.stockmonitor.models.entities.MonitoredStock;
import com.jruchel.stockmonitor.models.entities.User;
import com.jruchel.stockmonitor.security.SecurityService;
import com.jruchel.stockmonitor.services.stocks.MonitoredStockService;
import com.jruchel.stockmonitor.services.users.RoleService;
import com.jruchel.stockmonitor.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StartupService {

    private final GeneralProperties properties;
    private final MonitoredStockService monitoredStockService;
    private final RoleService roleService;
    private final SecurityService securityService;
    private final UserService userService;

    @EventListener(ApplicationReadyEvent.class)
    public void startup() {
        setupRoles();
        createAdminAccount();
        for (MonitoredStock stock : properties.getStocks()) {
            User user = userService.getByUsername(properties.getAdmin().getUsername());
            monitoredStockService.saveStockForUser(stock, user);
        }
    }

    private void setupRoles() {
        roleService.createUserRole();
        roleService.createAdminRole();
    }

    private void createAdminAccount() {
        GeneralProperties.Admin admin = properties.getAdmin();
        securityService.registerAdmin(admin.getUsername(), admin.getEmail(), admin.getPassword());
    }

}
