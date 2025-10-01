package com.inkorcloud.imlitejava.service.login.exception;

import lombok.Getter;

@Getter
public class SignNotMatch extends LoginFail {
    private Integer id;
    public SignNotMatch(String message) {
        super(message);
    }
    public SignNotMatch(String message, Integer id) {
      super(message);
      this.id = id;
    }
}
