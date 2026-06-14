package com.agrichain.collectioncenter;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionCenterRepository extends JpaRepository<CollectionCenter, Long> {
    boolean existsByName(String name);
}
