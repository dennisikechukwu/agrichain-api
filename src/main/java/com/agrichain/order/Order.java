package com.agrichain.order;

import com.agrichain.collectioncenter.CollectionCenter;
import com.agrichain.common.BaseEntity;
import com.agrichain.offtaker.OffTaker;
import com.agrichain.payment.Payment;
import com.agrichain.produce.Produce;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// "order" is a reserved SQL keyword in PostgreSQL — @Table(name = "order_tbl") avoids the conflict
@Entity
@Table(name = "order_tbl")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "off_taker_id", nullable = false)
    @ToString.Exclude
    private OffTaker offTaker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produce_id", nullable = false)
    @ToString.Exclude
    private Produce produce;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_center_id", nullable = false)
    @ToString.Exclude
    private CollectionCenter collectionCenter;

    // How much produce the off-taker is ordering (in the produce's unit, e.g. 500 KG)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantityRequested;

    // totalAmount = quantityRequested × produce.pricePerUnit — calculated in the service
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Payment payment;
}
