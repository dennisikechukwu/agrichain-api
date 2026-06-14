package com.agrichain.farmer;

import com.agrichain.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FarmerServiceImpl implements FarmerService {

    private final FarmerRepository farmerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<FarmerResponse> getAllFarmers() {
        return farmerRepository.findAll()
                .stream()
                .map(this::toFarmerResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FarmerResponse getFarmerById(Long id) {
        Farmer farmer = farmerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer with id " + id + " not found"));
        return toFarmerResponse(farmer);
    }

    private FarmerResponse toFarmerResponse(Farmer farmer) {
        return new FarmerResponse(
                farmer.getId(),
                farmer.getUser().getEmail(),
                farmer.getFirstName(),
                farmer.getLastName(),
                farmer.getPhone(),
                farmer.getState(),
                farmer.getLga(),
                farmer.getFarmSize(),
                farmer.getCreatedAt()
        );
    }
}
