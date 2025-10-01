package com.inkorcloud.imlitejava.controller.account.exception;

public class TokenExpire extends RuntimeException {
    public TokenExpire(String message) {
        super(message);
    }
}
