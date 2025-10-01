package com.inkorcloud.imlitejava.service.data.account.exception;

import com.inkorcloud.imlitejava.service.exception.ArgsInvalidException;
import lombok.Getter;

@Getter
public class PasswordFormatException extends ArgsInvalidException {
    private Integer accountId;
    private String passwd;
    public PasswordFormatException(String message) {
        super(message);
    }
    public PasswordFormatException(Integer accountId, String passwd, String message) {
        super(message);
        this.accountId = accountId;
        this.passwd = passwd;
    }
}
