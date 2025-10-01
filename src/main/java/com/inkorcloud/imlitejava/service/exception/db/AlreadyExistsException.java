package com.inkorcloud.imlitejava.service.exception.db;

import com.inkorcloud.imlitejava.service.exception.ArgsInvalidException;

public class AlreadyExistsException extends ArgsInvalidException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
