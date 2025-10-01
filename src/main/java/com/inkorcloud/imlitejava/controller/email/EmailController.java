package com.inkorcloud.imlitejava.controller.email;

import com.inkorcloud.imlitejava.service.email.EmailResponse;
import com.inkorcloud.imlitejava.service.email.EmailServicer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {
    private final EmailServicer emailServicer;
    public EmailController(@Autowired EmailServicer emailServicer) {
        this.emailServicer = emailServicer;
    }
    @RequestMapping("query_code")
    public EmailResponse QueryCode(String email) {
        return emailServicer.CodeSend(email);
    }
}
