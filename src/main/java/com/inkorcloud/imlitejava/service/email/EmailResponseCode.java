package com.inkorcloud.imlitejava.service.email;

import lombok.Getter;

@Getter
public enum EmailResponseCode {
    SUCCESS(0),
    FIELD_IS_NOT_EMAIL(1),
    TOO_MANY_REQUESTS(2),;
    private final int code;
    EmailResponseCode(int code) {
        this.code = code;
    }
}
