package com.inkorcloud.imlitejava.service.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginWithEmailRequest implements TimeGetter {
    @Email
    private String email;
    @Min(0)
    private Long num;
    @NotNull
    private Long time;
    @NotNull
    private String passwd;
}
