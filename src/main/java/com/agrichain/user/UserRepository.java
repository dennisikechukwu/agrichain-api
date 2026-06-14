package com.agrichain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// JpaRepository<User, Long> gives us save(), findById(), findAll(), delete() etc. for free
// The second type parameter (Long) must match the type of the @Id field in User (which extends BaseEntity)
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA reads the method name and generates the SQL automatically:
    // SELECT * FROM users WHERE email = ?
    // You don't write any SQL — the method name IS the query
    Optional<User> findByEmail(String email);
}
