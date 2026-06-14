package com.agrichain.collectioncenter;

import java.util.List;

public interface CollectionCenterService {
    CollectionCenterResponse createCollectionCenter(CollectionCenterRequest request);
    List<CollectionCenterResponse> getAllCollectionCenters();
    CollectionCenterResponse getCollectionCenterById(Long id);
}
