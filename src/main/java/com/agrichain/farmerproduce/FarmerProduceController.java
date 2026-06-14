package com.agrichain.farmerproduce;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/farmer-produce")
@RequiredArgsConstructor
@Tag(name = "Farmer Produce", description = "Link farmers to the produce they supply and track available quantities")
public class FarmerProduceController {

    private final FarmerProduceService farmerProduceService;

    @PostMapping
    @Operation(summary = "Add produce for a farmer", description = "Records that a farmer is supplying a specific produce type with an available quantity for a given season.")
    public ResponseEntity<FarmerProduceResponse> addFarmerProduce(@Valid @RequestBody FarmerProduceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(farmerProduceService.addFarmerProduce(request));
    }

    @GetMapping("/farmer/{farmerId}")
    @Operation(summary = "Get produce by farmer", description = "Returns all produce entries recorded for a specific farmer.")
    public ResponseEntity<List<FarmerProduceResponse>> getFarmerProduceByFarmerId(@PathVariable Long farmerId) {
        return ResponseEntity.ok(farmerProduceService.getFarmerProduceByFarmerId(farmerId));
    }
}
