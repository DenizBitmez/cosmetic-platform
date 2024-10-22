package com.cosmeticPlatform.CosmeticPlatform.model.request;

import lombok.Getter;
import lombok.Setter;
import com.cosmeticPlatform.CosmeticPlatform.model.UserType;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserRequestDTO {
    @NotNull(message = "İsim boş bırakılamaz.")
    private String username;

    @NotNull(message = "şifre boş bırakılamaz.")
    private String password;

    @NotNull(message = "Email boş bırakılamaz.")
    private String email;

    private UserType userType;

}
