package com.inkorcloud.imlitejava.service.login.exception;

import lombok.Getter;

@Getter
public class RequestTimeOut extends RuntimeException {
    private Integer id;
    public RequestTimeOut(String message) {
        super(message);
    }
    public RequestTimeOut(Integer id, String message) {
        super(message);
        this.id = id;
    }
}
