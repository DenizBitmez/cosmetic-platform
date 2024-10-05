package com.cosmeticPlatform.CosmeticPlatform.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RatingRequestDTO {
    @NotNull(message = "id boş bırakılamaz.")
    private Long id;

    @NotNull(message = "skor boş bırakılamaz.")
    private int score;
}
