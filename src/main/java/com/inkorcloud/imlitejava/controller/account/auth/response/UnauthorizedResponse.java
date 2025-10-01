package com.inkorcloud.imlitejava.controller.account.auth.response;

import lombok.Data;

//0, token expired
//1, permission denied

@Data
public class UnauthorizedResponse {
    Integer code;
    String message;
}
