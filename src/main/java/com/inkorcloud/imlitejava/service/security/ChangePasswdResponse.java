package com.inkorcloud.imlitejava.service.security;

import lombok.Data;

@Data
public class ChangePasswdResponse {
    private Integer id;
    private ChangePasswdResponseCode code;
    private String message;
}
