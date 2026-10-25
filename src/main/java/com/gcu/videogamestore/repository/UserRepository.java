package com.gcu.videogamestore.repository;

import com.gcu.videogamestore.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Data-access abstraction for persisted application users.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameIgnoreCase(String username);
}