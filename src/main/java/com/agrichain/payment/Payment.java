package com.agrichain.payment;

import com.agrichain.common.BaseEntity;
import com.agrichain.farmer.Farmer;
import com.agrichain.order.Order;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Payment represents money owed to or paid to a Farmer after an order is delivered.
// It is created when an Order reaches DELIVERED status.
@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity {

    // The farmer receiving this payment
    // FK "farmer_id" lives in THIS table (payments)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    @ToString.Exclude
    private Farmer farmer;

    // The specific order this payment is for — one order maps to one payment
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    @ToString.Exclude
    private Order order;

    // The amount to be paid or that was paid to the farmer
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    // Whether this payment is still pending or has been released to the farmer
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    // The date the payment was actually sent/processed
    // Can be null while status is PENDING, set when status changes to PAID
    private LocalDateTime paymentDate;
}
