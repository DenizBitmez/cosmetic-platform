package com.cosmeticPlatform.CosmeticPlatform.service;

import com.cosmeticPlatform.CosmeticPlatform.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import com.cosmeticPlatform.CosmeticPlatform.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment processPayment(Long orderId, String paymentType, double amount) {
        // Mock payment processing
        Payment payment = new Payment();
        payment.setPaymentType(paymentType);
        payment.setAmount(amount);
        // We would link Order here if we had OrderRepository access,
        // but for now let's just save it.
        // Actually, better to pass Order object or use ID.
        return paymentRepository.save(payment);
    }

    public Payment addPayment(Payment payment) {
        paymentRepository.save(payment);
        return payment;
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ödeme bulunamadı" + id));
    }

    public List<Payment> getAllPayment() {
        return paymentRepository.findAll();
    }
}
