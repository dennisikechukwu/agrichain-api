package com.agrichain.farmerproduce;

import com.agrichain.common.exception.ResourceNotFoundException;
import com.agrichain.farmer.Farmer;
import com.agrichain.farmer.FarmerRepository;
import com.agrichain.produce.Produce;
import com.agrichain.produce.ProduceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FarmerProduceServiceImpl implements FarmerProduceService {

    private final FarmerProduceRepository farmerProduceRepository;
    private final FarmerRepository farmerRepository;
    private final ProduceRepository produceRepository;

    @Override
    public FarmerProduceResponse addFarmerProduce(FarmerProduceRequest request) {

        // Verify the farmer exists before linking
        Farmer farmer = farmerRepository.findById(request.farmerId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer with id " + request.farmerId() + " not found"));

        // Verify the produce type exists in the catalogue
        Produce produce = produceRepository.findById(request.produceId())
                .orElseThrow(() -> new ResourceNotFoundException("Produce with id " + request.produceId() + " not found"));

        FarmerProduce farmerProduce = FarmerProduce.builder()
                .farmer(farmer)
                .produce(produce)
                .quantityAvailable(request.quantityAvailable())
                .season(request.season())
                .build();

        return toFarmerProduceResponse(farmerProduceRepository.save(farmerProduce));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FarmerProduceResponse> getFarmerProduceByFarmerId(Long farmerId) {

        // Confirm the farmer actually exists before querying their produce
        if (!farmerRepository.existsById(farmerId)) {
            throw new ResourceNotFoundException("Farmer with id " + farmerId + " not found");
        }

        return farmerProduceRepository.findByFarmer_Id(farmerId)
                .stream()
                .map(this::toFarmerProduceResponse)
                .toList();
    }

    private FarmerProduceResponse toFarmerProduceResponse(FarmerProduce fp) {
        return new FarmerProduceResponse(
                fp.getId(),
                fp.getFarmer().getId(),
                fp.getFarmer().getFirstName() + " " + fp.getFarmer().getLastName(),
                fp.getProduce().getId(),
                fp.getProduce().getName(),
                fp.getProduce().getUnit(),
                fp.getQuantityAvailable(),
                fp.getSeason(),
                fp.getCreatedAt()
        );
    }
}
