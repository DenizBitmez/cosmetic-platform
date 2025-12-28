package com.cosmeticPlatform.CosmeticPlatform.model.request;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class CommentRequestDTO {

    @NotNull(message = "id boş bırakılamaz.")
    private Long id;

    @NotNull(message = "message boş bırakılamaz.")
    private String content;
}
