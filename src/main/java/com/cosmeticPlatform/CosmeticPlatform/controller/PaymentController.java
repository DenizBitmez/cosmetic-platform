package com.cosmeticPlatform.CosmeticPlatform.controller;

import jakarta.validation.Valid;
import com.cosmeticPlatform.CosmeticPlatform.model.Payment;
import com.cosmeticPlatform.CosmeticPlatform.model.request.PaymentRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.cosmeticPlatform.CosmeticPlatform.service.PaymentService;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService){
    this.paymentService=paymentService;
}

@PostMapping("/add")
@ResponseStatus(HttpStatus.CREATED)
public Payment addPayment(@Valid @RequestBody PaymentRequestDTO paymentRequestDTO){
    Payment payment=new Payment();
    payment.setName(paymentRequestDTO.getName());
    payment.setId(paymentRequestDTO.getId());
    payment.setPaymentType(payment.getPaymentType());
    payment.setAmount(paymentRequestDTO.getAmount());
    return paymentService.addPayment(payment);
}

@GetMapping("/{id}")
public Payment getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);}

@GetMapping("/all")
@ResponseStatus(HttpStatus.OK)
    public List<Payment> getAllPayment(){
        return paymentService.getAllPayment();
    }
}



