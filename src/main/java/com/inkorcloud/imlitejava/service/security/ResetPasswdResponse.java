package com.inkorcloud.imlitejava.service.security;

import lombok.Data;

@Data
public class ResetPasswdResponse {
    private Integer id;
    private ResetPasswdResponseCode code;
    private String message;
}
