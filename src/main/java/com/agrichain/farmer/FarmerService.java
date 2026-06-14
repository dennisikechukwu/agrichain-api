package com.agrichain.farmer;

import java.util.List;

public interface FarmerService {

    List<FarmerResponse> getAllFarmers();

    FarmerResponse getFarmerById(Long id);
}
