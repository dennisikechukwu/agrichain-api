package com.agrichain.farmer;

import com.agrichain.common.BaseEntity;
import com.agrichain.farmerproduce.FarmerProduce;
import com.agrichain.payment.Payment;
import com.agrichain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "farmers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Farmer extends BaseEntity {

    // @OneToOne: one Farmer has exactly one User account
    // @JoinColumn: this is the "owning" side, meaning the FK column "user_id"
    // lives in the farmers table (not the users table)
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String phone;

    // State is stored as a plain String (e.g. "Lagos", "Kano", "Rivers")
    // No separate endpoint needed — the client sends the state name directly
    @Column(nullable = false)
    private String state;

    // LGA (Local Government Area) is a subdivision of a state in Nigeria.
    // Also stored as a plain String — the client sends it directly (e.g. "Ikeja", "Alimosho")
    @Column(nullable = false)
    private String lga;

    // Farm size is free text to allow flexibility: "5 acres", "2 hectares", "3 plots"
    @Column(nullable = false)
    private String farmSize;

    // @OneToMany: one Farmer can have many FarmerProduce entries (they supply multiple produce types)
    // mappedBy = "farmer" tells JPA: the FK lives on the FarmerProduce side, not here
    // cascade = ALL means if a Farmer is deleted, all their FarmerProduce records are also deleted
    // fetch = LAZY means FarmerProduce list is NOT loaded from DB until you explicitly access it
    // This is the default and is important for performance — don't load everything eagerly
    @OneToMany(mappedBy = "farmer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude  // Prevents infinite loop: Farmer → FarmerProduce → Farmer → ...
    private List<FarmerProduce> farmerProduces;

    // One Farmer can receive many Payments (one per delivered order)
    @OneToMany(mappedBy = "farmer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Payment> payments;
}
