package com.agrichain.payment;

import com.agrichain.common.exception.DuplicateResourceException;
import com.agrichain.common.exception.ResourceNotFoundException;
import com.agrichain.farmer.Farmer;
import com.agrichain.farmer.FarmerRepository;
import com.agrichain.order.Order;
import com.agrichain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final FarmerRepository farmerRepository;

    @Override
    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order with id " + request.orderId() + " not found."));

        if (paymentRepository.existsByOrder_Id(request.orderId())) {
            throw new DuplicateResourceException("A payment for order " + request.orderId() + " already exists.");
        }

        Farmer farmer = farmerRepository.findById(request.farmerId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer with id " + request.farmerId() + " not found."));

        Payment payment = Payment.builder()
                .order(order)
                .farmer(farmer)
                .amount(order.getTotalAmount())
                .status(PaymentStatus.PENDING)
                .paymentDate(null)
                .build();

        Payment saved = paymentRepository.save(payment);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment with id " + id + " not found."));
        return toResponse(payment);
    }

    @Override
    @Transactional
    public PaymentResponse updatePaymentStatus(Long id, UpdatePaymentStatusRequest request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment with id " + id + " not found."));

        // PAID is a terminal state — money has already moved, this cannot be reversed
        if (payment.getStatus() == PaymentStatus.PAID) {
            throw new IllegalArgumentException("Cannot change the status of a payment that has already been marked as PAID.");
        }

        payment.setStatus(request.status());

        // Only stamp paymentDate when money actually moves — clear it for any other status
        if (request.status() == PaymentStatus.PAID) {
            payment.setPaymentDate(LocalDateTime.now());
        } else {
            payment.setPaymentDate(null);
        }

        Payment saved = paymentRepository.save(payment);
        return toResponse(saved);
    }

    private PaymentResponse toResponse(Payment payment) {
        String farmerName = payment.getFarmer().getFirstName() + " " + payment.getFarmer().getLastName();

        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getFarmer().getId(),
                farmerName,
                payment.getAmount(),
                payment.getStatus(),
                payment.getPaymentDate(),
                payment.getCreatedAt()
        );
    }
}
