package com.inkorcloud.imlitejava.service.email.exception;

import lombok.Getter;

@Getter
public class FieldIsNotEmail extends RuntimeException {
    private String field;
    public FieldIsNotEmail(String message) {
        super(message);
    }
    public FieldIsNotEmail(String field, String message) {
        super(message);
        this.field = field;
    }
}
