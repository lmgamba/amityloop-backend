package com.lmgamba.amityloop.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// Marks this interface as a Spring-managed repository (data access layer)
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data generates the SQL automatically from the method name:
    // SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);
}