package com.inkorcloud.imlitejava.service.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswdRequest {
    @NotNull
    @Email
    private String email;
    @NotNull
    @Size(min = 6, max = 16)
    private String password;
    @NotNull
    private String code;
}
