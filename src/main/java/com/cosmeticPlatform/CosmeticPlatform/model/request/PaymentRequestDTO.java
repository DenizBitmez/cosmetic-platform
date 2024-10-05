package com.cosmeticPlatform.CosmeticPlatform.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PaymentRequestDTO {
    @NotNull(message = "name boş bırakılamaz.")
    private String name;

    @NotNull(message = "id boş bırakılamaz.")
    private Long id;

    @NotNull(message = "type boş bırakılamaz.")
    private String paymentType;

    @NotNull(message = "miktar boş bırakılamaz.")
    private double amount;
}
