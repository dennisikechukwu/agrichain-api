package com.agrichain.offtaker;

import java.util.List;

public interface OffTakerService {

    List<OffTakerResponse> getAllOffTakers();

    OffTakerResponse getOffTakerById(Long id);
}
