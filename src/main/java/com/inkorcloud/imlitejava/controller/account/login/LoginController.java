package com.inkorcloud.imlitejava.controller.account.login;

import com.inkorcloud.imlitejava.service.login.LoginRequest;
import com.inkorcloud.imlitejava.service.login.LoginResponse;
import com.inkorcloud.imlitejava.service.login.LoginService;
import com.inkorcloud.imlitejava.service.login.LoginWithEmailRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class LoginController {
    private final LoginService loginService;
    public LoginController(@Autowired LoginService loginService) {
        this.loginService = loginService;
    }

    @RequestMapping(value = "passwd", method = RequestMethod.POST)
    //sign = SHA256(id + passwd + time + num)
    public LoginResponse Login(@RequestBody @Validated LoginRequest request) {
        return loginService.Login(request);
    }

    @RequestMapping(value = "email", method = RequestMethod.POST)
    public LoginResponse LoginWithEmail(@RequestBody @Validated LoginWithEmailRequest request) {
        return loginService.LoginWithEmail(request);
    }
}
