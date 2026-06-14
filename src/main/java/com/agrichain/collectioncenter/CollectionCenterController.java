package com.agrichain.collectioncenter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/collection-centers")
@RequiredArgsConstructor
@Tag(name = "Collection Centers", description = "Manage physical hubs where farmers deliver their produce for off-taker orders")
public class CollectionCenterController {

    private final CollectionCenterService collectionCenterService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a collection center", description = "Registers a new collection center and assigns an admin user as its manager. Admin only.")
    public ResponseEntity<CollectionCenterResponse> createCollectionCenter(
            @Valid @RequestBody CollectionCenterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(collectionCenterService.createCollectionCenter(request));
    }

    @GetMapping
    @Operation(summary = "Get all collection centers", description = "Returns a list of all registered collection centers.")
    public ResponseEntity<List<CollectionCenterResponse>> getAllCollectionCenters() {
        return ResponseEntity.ok(collectionCenterService.getAllCollectionCenters());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get collection center by ID", description = "Returns details of a single collection center by its ID.")
    public ResponseEntity<CollectionCenterResponse> getCollectionCenterById(@PathVariable Long id) {
        return ResponseEntity.ok(collectionCenterService.getCollectionCenterById(id));
    }
}
