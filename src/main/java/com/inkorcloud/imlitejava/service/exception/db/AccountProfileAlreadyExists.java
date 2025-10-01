package com.inkorcloud.imlitejava.service.exception.db;

import lombok.Getter;

@Getter
public class AccountProfileAlreadyExists extends AlreadyExistsException {
    private Integer accountId;
    public AccountProfileAlreadyExists(Integer accountId, String message) {
        super(message);
        this.accountId = accountId;
    }
    public AccountProfileAlreadyExists(String message) {
        super(message);
    }
}
