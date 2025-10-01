package com.inkorcloud.imlitejava.controller.account.register;

import com.inkorcloud.imlitejava.service.register.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/register")
@RestController
public class RegisterController {
    private final RegisterService registerService;

    public RegisterController(@Autowired RegisterService registerService) {
        this.registerService = registerService;
    }


    @RequestMapping(value = "email", method = RequestMethod.POST)
    public RegisterResponse Register(@RequestBody @Validated RegisterRequest request) {
        return registerService.Register(request);
    }
}
