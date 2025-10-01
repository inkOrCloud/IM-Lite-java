package com.inkorcloud.imlitejava.service.login;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class LoginRequest implements TimeGetter {
    @NotNull
    private Integer id;
    @NotNull
    @Min(0)
    private Long num;
    @NotNull
    private Long time;
    @NotNull
    private String passwd;
}
