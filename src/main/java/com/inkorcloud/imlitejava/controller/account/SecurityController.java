package com.inkorcloud.imlitejava.controller.account;

import com.inkorcloud.imlitejava.controller.account.auth.UserInfoProvider;
import com.inkorcloud.imlitejava.util.jwt.JWTManager;
import com.inkorcloud.imlitejava.service.security.*;
import com.inkorcloud.imlitejava.util.jwt.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/security")
public class SecurityController {
    JWTManager jwtManager;
    SecurityService securityService;
    public SecurityController(@Autowired SecurityService securityService,
                              @Autowired JWTManager jwtManager) {
        this.securityService = securityService;
        this.jwtManager = jwtManager;
    }

    @RequestMapping("reset")
    public ResetPasswdResponse ResetPasswd(@Validated @RequestBody ResetPasswdRequest request) {
        return securityService.ResetPasswd(request);
    }

    @RequestMapping("change_passwd")
    public ChangePasswdResponse ChangePasswd(@Validated @RequestBody ChangePasswdRequest request,
                                             @UserInfoProvider UserInfo userInfo) {
        request.setId(userInfo.getUserId());
        return securityService.ChangePasswd(request);
    }
}
