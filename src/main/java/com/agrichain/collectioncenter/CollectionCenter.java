package com.agrichain.collectioncenter;

import com.agrichain.common.BaseEntity;
import com.agrichain.order.Order;
import com.agrichain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "collection_centers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectionCenter extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "manager_id", nullable = false)
    private User manager;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Integer capacity;

    @OneToMany(mappedBy = "collectionCenter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Order> orders;
}
