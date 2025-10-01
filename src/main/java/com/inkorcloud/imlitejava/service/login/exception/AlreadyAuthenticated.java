package com.inkorcloud.imlitejava.service.login.exception;

import lombok.Getter;

@Getter
public class AlreadyAuthenticated extends LoginFail {
    private String email;
    private Integer id;
    public AlreadyAuthenticated(String message) {
        super(message);
    }
    public AlreadyAuthenticated(String message, Integer id) {
      super(message);
      this.id = id;
    }
    public AlreadyAuthenticated(String message, String email) {
        super(message);
        this.email = email;
    }
}
