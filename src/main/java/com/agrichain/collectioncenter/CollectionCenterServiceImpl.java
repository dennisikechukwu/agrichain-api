package com.agrichain.collectioncenter;

import com.agrichain.common.exception.DuplicateResourceException;
import com.agrichain.common.exception.ResourceNotFoundException;
import com.agrichain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionCenterServiceImpl implements CollectionCenterService {

    private final CollectionCenterRepository collectionCenterRepository;

    @Override
    @Transactional
    public CollectionCenterResponse createCollectionCenter(CollectionCenterRequest request) {
        if (collectionCenterRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("A collection center with the name '" + request.name() + "' already exists.");
        }

        User manager = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CollectionCenter center = CollectionCenter.builder()
                .manager(manager)
                .name(request.name())
                .state(request.state())
                .address(request.address())
                .capacity(request.capacity())
                .build();

        CollectionCenter saved = collectionCenterRepository.save(center);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CollectionCenterResponse> getAllCollectionCenters() {
        return collectionCenterRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CollectionCenterResponse getCollectionCenterById(Long id) {
        CollectionCenter center = collectionCenterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Collection center with id " + id + " not found."));
        return toResponse(center);
    }

    private CollectionCenterResponse toResponse(CollectionCenter center) {
        return new CollectionCenterResponse(
                center.getId(),
                center.getName(),
                center.getState(),
                center.getAddress(),
                center.getCapacity(),
                center.getManager().getEmail(),
                center.getCreatedAt()
        );
    }
}
