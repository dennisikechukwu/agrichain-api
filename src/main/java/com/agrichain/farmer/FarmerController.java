package com.agrichain.farmer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/farmers")
@RequiredArgsConstructor
@Tag(name = "Farmers", description = "Endpoints for viewing registered farmers")
public class FarmerController {

    private final FarmerService farmerService;

    @GetMapping
    @Operation(summary = "Get all farmers", description = "Returns a list of all registered farmers with their profile details.")
    public ResponseEntity<List<FarmerResponse>> getAllFarmers() {
        return ResponseEntity.ok(farmerService.getAllFarmers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get farmer by ID", description = "Returns a single farmer profile by their farmer ID.")
    public ResponseEntity<FarmerResponse> getFarmerById(@PathVariable Long id) {
        return ResponseEntity.ok(farmerService.getFarmerById(id));
    }
}
