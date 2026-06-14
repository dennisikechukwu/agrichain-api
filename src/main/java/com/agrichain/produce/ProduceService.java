package com.agrichain.produce;

import java.util.List;

public interface ProduceService {

    List<ProduceResponse> getAllProduce();

    ProduceResponse getProduceById(Long id);

    ProduceResponse createProduce(ProduceRequest request);
}
