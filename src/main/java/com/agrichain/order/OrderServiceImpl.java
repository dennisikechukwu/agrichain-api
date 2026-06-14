package com.agrichain.order;

import com.agrichain.collectioncenter.CollectionCenter;
import com.agrichain.collectioncenter.CollectionCenterRepository;
import com.agrichain.common.exception.ResourceNotFoundException;
import com.agrichain.offtaker.OffTaker;
import com.agrichain.offtaker.OffTakerRepository;
import com.agrichain.produce.Produce;
import com.agrichain.produce.ProduceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OffTakerRepository offTakerRepository;
    private final ProduceRepository produceRepository;
    private final CollectionCenterRepository collectionCenterRepository;

    @Override
    @Transactional
    public OrderResponse placeOrder(OrderRequest request) {
        OffTaker offTaker = offTakerRepository.findById(request.offTakerId())
                .orElseThrow(() -> new ResourceNotFoundException("Off-taker with id " + request.offTakerId() + " not found."));

        Produce produce = produceRepository.findById(request.produceId())
                .orElseThrow(() -> new ResourceNotFoundException("Produce with id " + request.produceId() + " not found."));

        CollectionCenter collectionCenter = collectionCenterRepository.findById(request.collectionCenterId())
                .orElseThrow(() -> new ResourceNotFoundException("Collection center with id " + request.collectionCenterId() + " not found."));

        // Auto-calculate total: quantity × price per unit
        BigDecimal totalAmount = produce.getPricePerUnit().multiply(request.quantityRequested());

        Order order = Order.builder()
                .offTaker(offTaker)
                .produce(produce)
                .collectionCenter(collectionCenter)
                .quantityRequested(request.quantityRequested())
                .totalAmount(totalAmount)
                .status(OrderStatus.PENDING)
                .orderDate(LocalDateTime.now())
                .build();

        Order saved = orderRepository.save(order);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order with id " + id + " not found."));
        return toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long id, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order with id " + id + " not found."));

        // DELIVERED and CANCELLED are terminal states — the order lifecycle is over
        if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException(
                    "Cannot update the status of an order that is already " + order.getStatus().name() + "."
            );
        }

        order.setStatus(request.status());
        Order saved = orderRepository.save(order);
        return toResponse(saved);
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOffTaker().getId(),
                order.getOffTaker().getCompanyName(),
                order.getProduce().getId(),
                order.getProduce().getName(),
                order.getProduce().getUnit(),
                order.getCollectionCenter().getId(),
                order.getCollectionCenter().getName(),
                order.getQuantityRequested(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getOrderDate(),
                order.getCreatedAt()
        );
    }
}
