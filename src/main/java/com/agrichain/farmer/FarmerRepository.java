package com.agrichain.farmer;

import com.agrichain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FarmerRepository extends JpaRepository<Farmer, Long> {

    // Used in /me endpoint to load a farmer's profile from their User object
    Optional<Farmer> findByUser(User user);
}
