package com.inkorcloud.imlitejava.service.email.exception;

import lombok.Getter;

@Getter
public class TooManyRequestsException extends RuntimeException {
    private String email;
    public TooManyRequestsException(String message) {
        super(message);
    }
    public TooManyRequestsException(String email, String message) {
        super(message);
        this.email = email;
    }
}
