package com.inkorcloud.imlitejava.service.exception;

import lombok.Getter;

@Getter
public class OperationException extends RuntimeException {
    public OperationException(String message) {
        super(message);
    }
}
