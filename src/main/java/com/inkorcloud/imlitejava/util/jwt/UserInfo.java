package com.inkorcloud.imlitejava.util.jwt;

import lombok.Data;

import java.security.Principal;

@Data
public class UserInfo implements Principal {
    private Integer userId;
    private Integer profileId;

    @Override
    public String getName() {
        return userId.toString();
    }
}
