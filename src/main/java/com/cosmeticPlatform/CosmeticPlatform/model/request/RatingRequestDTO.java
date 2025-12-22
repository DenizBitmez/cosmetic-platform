package com.cosmeticPlatform.CosmeticPlatform.model.request;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class RatingRequestDTO {

    private Integer userId;

    private Integer productId;

    @NotNull(message = "skor boş bırakılamaz.")
    private int score;
}
