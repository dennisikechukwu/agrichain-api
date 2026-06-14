package com.agrichain.offtaker;

import com.agrichain.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OffTakerServiceImpl implements OffTakerService {

    private final OffTakerRepository offTakerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<OffTakerResponse> getAllOffTakers() {
        return offTakerRepository.findAll()
                .stream()
                .map(this::toOffTakerResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OffTakerResponse getOffTakerById(Long id) {
        OffTaker offTaker = offTakerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OffTaker with id " + id + " not found"));
        return toOffTakerResponse(offTaker);
    }

    private OffTakerResponse toOffTakerResponse(OffTaker offTaker) {
        return new OffTakerResponse(
                offTaker.getId(),
                offTaker.getUser().getEmail(),
                offTaker.getCompanyName(),
                offTaker.getContactPerson(),
                offTaker.getPhone(),
                offTaker.getCreatedAt()
        );
    }
}
