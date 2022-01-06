package com.jruchel.stockmonitor.repositories;

import com.jruchel.stockmonitor.models.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    Role getByTitle(String title);

}
