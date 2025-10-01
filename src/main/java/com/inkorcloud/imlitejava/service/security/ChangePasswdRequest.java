package com.inkorcloud.imlitejava.service.security;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswdRequest {
    private Integer id;
    @NotNull
    private String oldPass;
    @NotNull
    @Size(min = 6, max = 16)
    private String newPass;
}
