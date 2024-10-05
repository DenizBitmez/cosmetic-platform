package com.cosmeticPlatform.CosmeticPlatform.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
@Getter
@Setter
public class ProductRequestDTO {
    @NotNull(message = "içerik boş bırakılamaz.")
    private String name;

    @NotNull(message = "id boş bırakılamaz.")
    private int id;

    @NotNull(message = "kategori boş bırakılamaz.")
    private String category;
}
