package com.agrichain.offtaker;

import com.agrichain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OffTakerRepository extends JpaRepository<OffTaker, Long> {

    Optional<OffTaker> findByUser(User user);
}
