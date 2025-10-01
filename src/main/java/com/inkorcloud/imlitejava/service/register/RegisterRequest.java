package com.inkorcloud.imlitejava.service.register;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotNull
    @Email
    private String email;
    @NotNull
    @Size(min = 6, max = 16)
    private String passwd;
    @NotNull
    private String code;
}
