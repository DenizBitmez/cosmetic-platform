package com.cosmeticPlatform.CosmeticPlatform.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequestDTO {
    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "City is mandatory")
    private String city;

    @NotBlank(message = "District is mandatory")
    private String district;

    @NotBlank(message = "Full address is mandatory")
    private String fullAddress;

    private Integer userId;
}
