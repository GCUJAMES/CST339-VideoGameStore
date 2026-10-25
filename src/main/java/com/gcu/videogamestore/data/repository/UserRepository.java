package com.gcu.videogamestore.data.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import com.gcu.videogamestore.data.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long>
{
	boolean existsByUsername(String username);
	boolean existsByEmailAddress(String emailAddress);
	Optional<UserEntity> findByUsername(String username);
}