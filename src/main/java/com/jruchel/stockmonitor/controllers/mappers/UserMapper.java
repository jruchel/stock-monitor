package com.jruchel.stockmonitor.controllers.mappers;

import com.jruchel.stockmonitor.controllers.models.UserATO;
import com.jruchel.stockmonitor.controllers.models.UserLoginRequest;
import com.jruchel.stockmonitor.controllers.models.UserRegistrationRequest;
import com.jruchel.stockmonitor.models.entities.User;
import com.jruchel.stockmonitor.services.users.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final RoleService roleService;

    public UserATO getUserATO(User user) {
        return new UserATO(user.getUsername(), user.getMonitoredStocks());
    }

    public User getUserFromRegistrationRequest(UserRegistrationRequest request) {
        return new User(null, request.getUsername(), request.getPassword(), request.getEmail(), new ArrayList<>(), Set.of(roleService.getUserRole()));
    }

    public User getUserFromLoginRequest(UserLoginRequest request) {
        return new User(null, request.getUsername(), request.getPassword(), "", new ArrayList<>(), Set.of(roleService.getUserRole()));
    }

}
