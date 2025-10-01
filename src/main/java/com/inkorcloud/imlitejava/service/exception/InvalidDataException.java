package com.inkorcloud.imlitejava.service.exception;

import lombok.Getter;

@Getter
public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String message) {
        super(message);
    }
}
