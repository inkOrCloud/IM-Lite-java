package com.inkorcloud.imlitejava.service.email.exception;

import lombok.Getter;

@Getter
public class InvalidVerificationCodeException extends RuntimeException {
    private String email;
    public InvalidVerificationCodeException(String message) {
        super(message);
    }
    public InvalidVerificationCodeException(String email, String message) {
      super(message);
      this.email = email;
    }
}
