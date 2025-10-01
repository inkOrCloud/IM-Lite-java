package com.inkorcloud.imlitejava.service.email;

import lombok.Data;

@Data
public class EmailResponse {
    private Integer code;
    private String message;
    private String email;
    public void setCode(EmailResponseCode code) {
        this.code = code.getCode();
    }
}
