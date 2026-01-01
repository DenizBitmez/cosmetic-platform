package com.cosmeticPlatform.CosmeticPlatform.controller;

import com.cosmeticPlatform.CosmeticPlatform.model.response.PaymentResponseDTO;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class StripeController {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody Map<String, Object> data) throws StripeException {
        System.out.println("Processing PaymentIntent request for amount: " + data.get("amount"));
        Stripe.apiKey = stripeApiKey;
        System.out.println("Using Stripe API Key starting with: "
                + (stripeApiKey != null ? stripeApiKey.substring(0, 7) : "NULL"));

        Double amountDouble = Double.valueOf(data.get("amount").toString());
        // Stripe expects amount in cents
        long amount = (long) (amountDouble * 100);

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency("usd")
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build())
                .build();

        PaymentIntent intent = PaymentIntent.create(params);

        return ResponseEntity.ok(new PaymentResponseDTO(intent.getClientSecret()));
    }

    @GetMapping("/config")
    public ResponseEntity<Map<String, String>> getConfig(@Value("${stripe.public.key}") String publicKey) {
        Map<String, String> config = new HashMap<>();
        config.put("publicKey", publicKey);
        return ResponseEntity.ok(config);
    }
}
