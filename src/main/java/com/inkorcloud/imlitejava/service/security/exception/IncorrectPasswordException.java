package com.inkorcloud.imlitejava.service.security.exception;

import lombok.Getter;

@Getter
public class IncorrectPasswordException extends RuntimeException {
    private Integer id;
    public IncorrectPasswordException(String message) {
        super(message);
    }
    public IncorrectPasswordException(Integer id, String message) {
      super(message);
    }
}
