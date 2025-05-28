package com.pdev.stocktracker.repository;

import com.pdev.stocktracker.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<UserDetails> findByEmail(String email);
}
