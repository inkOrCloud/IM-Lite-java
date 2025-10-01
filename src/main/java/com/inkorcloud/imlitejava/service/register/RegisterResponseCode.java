package com.inkorcloud.imlitejava.service.register;

import lombok.Getter;

@Getter
public enum RegisterResponseCode {
    SUCCESS(0),
    EMAIL_REGISTERED(1);
    private final int code;
    RegisterResponseCode(int code) {
        this.code = code;
    }
}
