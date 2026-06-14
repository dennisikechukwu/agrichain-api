package com.agrichain.offtaker;

import com.agrichain.common.BaseEntity;
import com.agrichain.order.Order;
import com.agrichain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "off_takers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OffTaker extends BaseEntity {

    // The FK "user_id" lives in the off_takers table
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Company name of the buyer/retailer (e.g. "Shoprite Nigeria", "FoodCo")
    @Column(nullable = false)
    private String companyName;

    // The person within the company who is the point of contact
    @Column(nullable = false)
    private String contactPerson;

    @Column(nullable = false)
    private String phone;

    // One OffTaker can place many Orders
    @OneToMany(mappedBy = "offTaker", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude  // Prevents circular reference in Lombok's toString
    private List<Order> orders;
}
