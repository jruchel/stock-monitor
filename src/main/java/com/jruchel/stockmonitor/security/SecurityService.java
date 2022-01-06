package com.jruchel.stockmonitor.security;

import com.jruchel.stockmonitor.config.security.JWTConfig;
import com.jruchel.stockmonitor.controllers.mappers.UserMapper;
import com.jruchel.stockmonitor.controllers.models.AuthToken;
import com.jruchel.stockmonitor.controllers.models.UserATO;
import com.jruchel.stockmonitor.controllers.models.UserLoginRequest;
import com.jruchel.stockmonitor.controllers.models.UserRegistrationRequest;
import com.jruchel.stockmonitor.errors.AuthenticationError;
import com.jruchel.stockmonitor.errors.RegistrationError;
import com.jruchel.stockmonitor.models.entities.User;
import com.jruchel.stockmonitor.security.jwt.JWTUtils;
import com.jruchel.stockmonitor.services.users.RoleService;
import com.jruchel.stockmonitor.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final JWTConfig jwtConfig;
    private final JWTUtils jwtUtils;
    private final UserMapper userMapper;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public AuthToken getAuthToken(UserLoginRequest userLoginRequest) {
        validateUser(userLoginRequest.getUsername(), userLoginRequest.getPassword());
        String token = jwtUtils.generateToken(userMapper.getUserFromLoginRequest(userLoginRequest));
        return new AuthToken(jwtConfig.getJwtHeaderName(), token);
    }

    public void validateUser(String username, String password) {
        User user = userService.getByUsername(username);
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new AuthenticationError("Incorrect login or password");
    }

    public User register(UserRegistrationRequest request) {
        if (userService.getByEmail(request.getEmail()) != null)
            throw new RegistrationError("An account is already registered for this email address");
        if (userService.getByUsername(request.getUsername()) != null)
            throw new RegistrationError("Username is already taken");
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        return userService.save(userMapper.getUserFromRegistrationRequest(request));
    }

    public User registerAdmin(String username, String email, String password) {
        if (userService.getByEmail(email) != null)
            throw new RegistrationError("An account is already registered for this email address");
        if (userService.getByUsername(username) != null)
            throw new RegistrationError("Username is already taken");
        password = passwordEncoder.encode(password);
        return userService.save(new User(null, username, password, email, new ArrayList<>(), Set.of(roleService.getUserRole(), roleService.getAdminRole())));
    }

    public User getCurrentUser() {
        return userService.getByUsername(((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
    }

}
