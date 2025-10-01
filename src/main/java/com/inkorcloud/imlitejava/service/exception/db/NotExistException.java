package com.inkorcloud.imlitejava.service.exception.db;

public class NotExistException extends RuntimeException {
    public NotExistException(String message) {
        super(message);
    }
}
