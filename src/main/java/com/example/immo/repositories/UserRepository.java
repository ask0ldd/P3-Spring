package com.example.immo.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.immo.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    // custom query
    Optional<User> findByEmail(String email);
}

// JPA Queries :
// https://docs.spring.io/spring-data/jpa/docs/1.5.0.RELEASE/reference/html/jpa.repositories.html