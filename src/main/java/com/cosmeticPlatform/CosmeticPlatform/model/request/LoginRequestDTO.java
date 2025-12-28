package com.cosmeticPlatform.CosmeticPlatform.model.request;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class LoginRequestDTO {
    @NotNull
    private String email;

    @NotNull
    private String password;
}
