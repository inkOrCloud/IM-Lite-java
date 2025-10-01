package com.inkorcloud.imlitejava.service.register;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.inkorcloud.imlitejava.entity.account.Account;
import com.inkorcloud.imlitejava.service.data.account.AccountManager;
import com.inkorcloud.imlitejava.entity.account.AccountProfile;
import com.inkorcloud.imlitejava.service.data.account.profile.AccountProfileManager;
import com.inkorcloud.imlitejava.service.email.EmailServicer;
import com.inkorcloud.imlitejava.service.email.exception.InvalidVerificationCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
    private final AccountManager accountManager;
    private final EmailServicer emailServicer;
    private final AccountProfileManager accountProfileManager;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    public RegisterService(@Autowired AccountManager accountManager,
                           @Autowired EmailServicer emailServicer,
                           @Autowired AccountProfileManager accountProfileManager) {
        this.accountManager = accountManager;
        this.emailServicer = emailServicer;
        this.accountProfileManager = accountProfileManager;
    }
    public RegisterResponse Register(RegisterRequest request) {
        RegisterResponse response = new RegisterResponse();
        if(request.getPasswd() == null || request.getPasswd().length() < 6) {
            logger.warn("register fail , password too short, email = {}", request.getEmail());
            throw new IllegalArgumentException("password too short");
        }
        String code = emailServicer.GetCode(request.getEmail());
        if(!code.equals(request.getCode())) {
            logger.warn("register fail , wrong email code, email = {}", request.getEmail());
            throw new InvalidVerificationCodeException(request.getEmail(), "wrong email code");
        }
        Account account = new Account();
        account.setPasswd(request.getPasswd());
        account = accountManager.CreateAccount(account);
        AccountProfile profile = new AccountProfile();
        profile.setUserId(account.getId());
        profile.setEmail(request.getEmail());
        profile.setName(RandomUtil.randomString(8));
        accountProfileManager.CreateAccountProfile(profile);
        response.setCode(RegisterResponseCode.SUCCESS);
        response.setMessage("success");
        response.setId(account.getId());
        logger.info("register success, email = {}", request.getEmail());
        return response;
    }
}
