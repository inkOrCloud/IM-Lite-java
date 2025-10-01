package com.inkorcloud.imlitejava.service.email.exception;

import lombok.Getter;

@Getter
public class VerificationCodeNotRequestedException extends RuntimeException {
    private String email;
    public VerificationCodeNotRequestedException(String message) {
        super(message);
    }
    public VerificationCodeNotRequestedException(String email, String message) {
      super(message);
      this.email = email;
    }
}
