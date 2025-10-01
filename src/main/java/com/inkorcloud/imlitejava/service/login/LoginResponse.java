package com.inkorcloud.imlitejava.service.login;

import lombok.Data;

@Data
public class LoginResponse {
    private Integer code;
    private String message;
    private Integer id;
    private Long time;
    private String token;

    public void setCode(LoginResponseCode code) {
        this.code = code.getCode();
    }
}
