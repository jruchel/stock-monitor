package com.jruchel.stockmonitor.services.users;

import com.jruchel.stockmonitor.models.entities.Role;
import com.jruchel.stockmonitor.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getUserRole() {
        return roleRepository.getByTitle("ROLE_USER");
    }

    public Role getAdminRole() {
        return roleRepository.getByTitle("ROLE_ADMIN");
    }


    public Role createUserRole() {
        if (roleRepository.getByTitle("ROLE_USER") == null)
            return roleRepository.save(new Role(null, "ROLE_USER"));
        throw new ValidationException("Role user already exists");
    }

    public Role createAdminRole() {
        if (roleRepository.getByTitle("ROLE_ADMIN") == null)
            return roleRepository.save(new Role(null, "ROLE_ADMIN"));
        throw new ValidationException("Role admin already exists");
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

}
