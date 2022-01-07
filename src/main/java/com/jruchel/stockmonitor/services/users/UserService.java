package com.jruchel.stockmonitor.services.users;

import com.jruchel.stockmonitor.config.GeneralProperties;
import com.jruchel.stockmonitor.controllers.mappers.UserMapper;
import com.jruchel.stockmonitor.models.entities.User;
import com.jruchel.stockmonitor.repositories.RoleRepository;
import com.jruchel.stockmonitor.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final GeneralProperties properties;

    public User getByUsername(String username) {
        return repository.getByUsername(username);
    }

    public User getAdminUser() {
        return repository.getByUsername(properties.getAdmin().getUsername());
    }

    public User getByEmail(String email) {
        return repository.getByEmail(email);
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User save(User user) {
        return repository.save(user);
    }

}
