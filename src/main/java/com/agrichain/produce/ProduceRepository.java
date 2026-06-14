package com.agrichain.produce;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduceRepository extends JpaRepository<Produce, Long> {

    boolean existsByName(String name);
}
