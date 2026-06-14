package com.agrichain.offtaker;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/offtakers")
@RequiredArgsConstructor
@Tag(name = "Off-Takers", description = "Endpoints for viewing registered off-takers (buyers)")
public class OffTakerController {

    private final OffTakerService offTakerService;

    @GetMapping
    @Operation(summary = "Get all off-takers", description = "Returns a list of all registered off-takers (companies that buy produce from farmers).")
    public ResponseEntity<List<OffTakerResponse>> getAllOffTakers() {
        return ResponseEntity.ok(offTakerService.getAllOffTakers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get off-taker by ID", description = "Returns a single off-taker profile by their off-taker ID.")
    public ResponseEntity<OffTakerResponse> getOffTakerById(@PathVariable Long id) {
        return ResponseEntity.ok(offTakerService.getOffTakerById(id));
    }
}
