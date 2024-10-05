package com.cosmeticPlatform.CosmeticPlatform.model.request;

import com.cosmeticPlatform.CosmeticPlatform.model.User;
import lombok.Getter;
import lombok.Setter;
import com.cosmeticPlatform.CosmeticPlatform.model.UserType;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserRequestDTO extends User {
    @NotNull(message = "İsim boş bırakılamaz.")
    private String username;

    @NotNull(message = "id boş bırakılamaz.")
    private int id;

    @NotNull(message = "şifre boş bırakılamaz.")
    private String password;

    @NotNull(message = "Email boş bırakılamaz.")
    private String email;

    private UserType userType;

}
