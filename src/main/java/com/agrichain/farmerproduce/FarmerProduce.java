package com.agrichain.farmerproduce;

import com.agrichain.common.BaseEntity;
import com.agrichain.farmer.Farmer;
import com.agrichain.produce.Produce;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

// This is a junction/association entity — it represents the relationship
// "Farmer X can supply Produce Y". It's not just a join table because it
// carries its own data (quantityAvailable, season), so it becomes a full entity.
@Entity
@Table(name = "farmer_produce")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FarmerProduce extends BaseEntity {

    // @ManyToOne: many FarmerProduce rows can point to the same Farmer
    // @JoinColumn: the FK "farmer_id" lives in THIS table (farmer_produce)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    @ToString.Exclude  // Prevents: FarmerProduce → Farmer → List<FarmerProduce> → Farmer → ...
    private Farmer farmer;

    // @ManyToOne: many FarmerProduce rows can point to the same Produce type
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produce_id", nullable = false)
    @ToString.Exclude
    private Produce produce;

    // How much of this produce the farmer currently has available (in the produce's unit)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantityAvailable;

    // The farming season this supply is associated with (e.g. "2024 Wet Season", "2025 Dry Season")
    @Column(nullable = false)
    private String season;
}
