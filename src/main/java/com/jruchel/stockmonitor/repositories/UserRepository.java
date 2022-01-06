package com.jruchel.stockmonitor.repositories;

import com.jruchel.stockmonitor.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User getByUsername(String username);

    User getByEmail(String email);

}
