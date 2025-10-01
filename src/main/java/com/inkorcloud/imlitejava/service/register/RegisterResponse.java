package com.inkorcloud.imlitejava.service.register;

import lombok.Data;

//0,成功
@Data
public class RegisterResponse {
    private Integer id;
    private Integer code;
    private String message;
    public void setCode(RegisterResponseCode code) {
        this.code = code.getCode();
    }
}
