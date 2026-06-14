package com.agrichain.produce;

import com.agrichain.common.exception.DuplicateResourceException;
import com.agrichain.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProduceServiceImpl implements ProduceService {

    private final ProduceRepository produceRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProduceResponse> getAllProduce() {
        return produceRepository.findAll()
                .stream()
                .map(this::toProduceResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProduceResponse getProduceById(Long id) {
        Produce produce = produceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produce with id " + id + " not found"));
        return toProduceResponse(produce);
    }

    @Override
    public ProduceResponse createProduce(ProduceRequest request) {
        if (produceRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Produce with name '" + request.name() + "' already exists in the catalogue");
        }

        Produce produce = Produce.builder()
                .name(request.name())
                .category(request.category())
                .unit(request.unit())
                .pricePerUnit(request.pricePerUnit())
                .build();

        return toProduceResponse(produceRepository.save(produce));
    }

    private ProduceResponse toProduceResponse(Produce produce) {
        return new ProduceResponse(
                produce.getId(),
                produce.getName(),
                produce.getCategory(),
                produce.getUnit(),
                produce.getPricePerUnit(),
                produce.getCreatedAt()
        );
    }
}
