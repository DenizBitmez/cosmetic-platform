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
    public PaymentService(PaymentRepository paymentRepository){
    this.paymentRepository=paymentRepository;
}

    public Payment addPayment(Payment payment){
        paymentRepository.save(payment);
        return payment;
}

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ödeme bulunamadı" + id));
    }

    public List<Payment> getAllPayment(){
        return paymentRepository.findAll();
    }
}

