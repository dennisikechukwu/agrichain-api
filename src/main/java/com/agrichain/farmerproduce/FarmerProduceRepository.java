package com.agrichain.farmerproduce;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FarmerProduceRepository extends JpaRepository<FarmerProduce, Long> {

    // findByFarmer_Id traverses the relationship: FarmerProduce → farmer → id
    // Spring Data JPA reads the underscore as a navigation: "find where farmer's id equals farmerId"
    List<FarmerProduce> findByFarmer_Id(Long farmerId);
}
