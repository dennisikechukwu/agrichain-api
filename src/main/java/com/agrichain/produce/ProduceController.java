package com.agrichain.produce;

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
@RequestMapping("/api/v1/produce")
@RequiredArgsConstructor
@Tag(name = "Produce", description = "Manage the agricultural produce catalogue")
public class ProduceController {

    private final ProduceService produceService;

    @GetMapping
    @Operation(summary = "Get all produce", description = "Returns the full catalogue of produce available on the platform.")
    public ResponseEntity<List<ProduceResponse>> getAllProduce() {
        return ResponseEntity.ok(produceService.getAllProduce());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get produce by ID", description = "Returns details for a single produce item including its price per unit.")
    public ResponseEntity<ProduceResponse> getProduceById(@PathVariable Long id) {
        return ResponseEntity.ok(produceService.getProduceById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create produce", description = "Adds a new produce item to the platform catalogue. Admin only. Produce names must be unique.")
    public ResponseEntity<ProduceResponse> createProduce(@Valid @RequestBody ProduceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produceService.createProduce(request));
    }
}
