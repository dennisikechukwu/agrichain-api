package com.agrichain.payment;

import java.util.List;

public interface PaymentService {
    PaymentResponse createPayment(PaymentRequest request);
    List<PaymentResponse> getAllPayments();
    PaymentResponse getPaymentById(Long id);
    PaymentResponse updatePaymentStatus(Long id, UpdatePaymentStatusRequest request);
}
