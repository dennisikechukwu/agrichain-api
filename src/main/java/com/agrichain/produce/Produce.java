package com.agrichain.produce;

import com.agrichain.common.BaseEntity;
import com.agrichain.farmerproduce.FarmerProduce;
import com.agrichain.order.Order;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "produce")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produce extends BaseEntity {

    // e.g. "Maize", "Cassava", "Yam", "Tomatoes"
    // unique = true prevents two identical produce types in the catalogue
    @Column(nullable = false, unique = true)
    private String name;

    // Broad grouping: e.g. "Grains", "Tubers", "Vegetables", "Fruits"
    @Column(nullable = false)
    private String category;

    // The unit of measurement used when pricing and ordering
    // Stored as a String for flexibility: "KG", "TONNES", "BAGS", "CRATES"
    @Column(nullable = false)
    private String unit;

    // Price per single unit (e.g. ₦350.00 per KG)
    // BigDecimal is used for money — never use float or double for currency
    // because they have rounding errors that accumulate over many calculations
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerUnit;

    // Farmers who supply this type of produce
    @OneToMany(mappedBy = "produce", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<FarmerProduce> farmerProduces;

    // Orders placed for this type of produce
    @OneToMany(mappedBy = "produce", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Order> orders;
}
