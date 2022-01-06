package com.jruchel.stockmonitor.controllers;

import com.jruchel.stockmonitor.controllers.mappers.UserMapper;
import com.jruchel.stockmonitor.controllers.models.AuthToken;
import com.jruchel.stockmonitor.controllers.models.UserATO;
import com.jruchel.stockmonitor.controllers.models.UserLoginRequest;
import com.jruchel.stockmonitor.controllers.models.UserRegistrationRequest;
import com.jruchel.stockmonitor.security.SecurityService;
import com.jruchel.stockmonitor.security.autoconfig.Controller;
import com.jruchel.stockmonitor.security.autoconfig.SecuredMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController extends Controller {

    private final UserMapper userMapper;
    private final SecurityService securityService;

    @SecuredMapping(path = "/me", method = RequestMethod.GET, role = "user")
    public ResponseEntity<UserATO> getPrincipal() {
        return ResponseEntity.ok(userMapper.getUserATO(securityService.getCurrentUser()));
    }

    @SecuredMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<UserATO> register(@RequestBody @Valid UserRegistrationRequest request) {
        return ResponseEntity.ok(userMapper.getUserATO(securityService.register(request)));
    }

    @SecuredMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<AuthToken> login(@RequestBody @Valid UserLoginRequest loginRequest) {
        return ResponseEntity.ok(securityService.getAuthToken(loginRequest));
    }

}
