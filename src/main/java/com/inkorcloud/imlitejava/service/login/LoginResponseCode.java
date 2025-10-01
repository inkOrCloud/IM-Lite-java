package com.inkorcloud.imlitejava.service.login;

import lombok.Getter;

@Getter
public enum LoginResponseCode {
    SUCCESS(0),
    SIGN_ERROR(1),
    REQUEST_TIMEOUT(2),
    DUPLICATE_REQUEST(3),
    INVALID_ARGS(4),;
    private final int code;
    private LoginResponseCode(int code) {
        this.code = code;
    }
}
