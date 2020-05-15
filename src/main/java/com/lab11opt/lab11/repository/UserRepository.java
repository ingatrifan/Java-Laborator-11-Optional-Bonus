package com.lab11opt.lab11.repository;

import com.lab11opt.lab11.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Integer> {

    Optional<User> findByUsername(String username);
}
