package com.agrichain.farmerproduce;

import java.util.List;

public interface FarmerProduceService {

    FarmerProduceResponse addFarmerProduce(FarmerProduceRequest request);

    List<FarmerProduceResponse> getFarmerProduceByFarmerId(Long farmerId);
}
