package com.inkorcloud.imlitejava.controller.email;

import lombok.Getter;

@Getter
public enum UnauthorizedCode {
    NOT_REQUEST(1, "not request code"),
    CODE_VERIFICATION_FAILED(2, "code verification failed"),;

    private final int code;
    private final String message;
    UnauthorizedCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
