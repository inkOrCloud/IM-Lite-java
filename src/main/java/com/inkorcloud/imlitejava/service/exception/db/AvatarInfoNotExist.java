package com.inkorcloud.imlitejava.service.exception.db;

import lombok.Getter;

@Getter
public class AvatarInfoNotExist extends NotExistException {
    private Integer id;
    public AvatarInfoNotExist(String message) {
        super(message);
    }
    public AvatarInfoNotExist(Integer id, String message) {
        super(message);
        this.id = id;
    }
}
