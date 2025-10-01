package com.inkorcloud.imlitejava.controller.account.auth;

import com.inkorcloud.imlitejava.util.jwt.UserInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @RequestMapping("vali_online_status")
    public Object valiOnlineStatus(@UserInfoProvider UserInfo userInfo) {
        return  userInfo;
    }
}
