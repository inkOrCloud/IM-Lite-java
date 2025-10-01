package com.inkorcloud.imlitejava.service.login;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.inkorcloud.imlitejava.util.jwt.JWTManager;
import com.inkorcloud.imlitejava.entity.account.Account;
import com.inkorcloud.imlitejava.service.data.account.AccountManager;
import com.inkorcloud.imlitejava.entity.account.AccountProfile;
import com.inkorcloud.imlitejava.service.data.account.profile.AccountProfileManager;
import com.inkorcloud.imlitejava.util.jwt.UserInfo;
import com.inkorcloud.imlitejava.service.login.exception.SignNotMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;


@Service
public class LoginService {
    private final Logger logger = LoggerFactory.getLogger(LoginService.class);
    private final Long expire;

    private final AccountManager accountManager;
    private final AccountProfileManager accountProfileManager;
    private final JWTManager jwtManager;

    public LoginService(@Autowired AccountManager accountManager,
                        @Value("${login.expire}") Long expire,
                        @Autowired AccountProfileManager accountProfileManager,
                        @Autowired JWTManager jwtManager) {
        this.accountManager = accountManager;
        this.expire = expire;
        this.accountProfileManager = accountProfileManager;
        this.jwtManager = jwtManager;
    }

    public LoginResponse Login(LoginRequest request) {
        Account account = accountManager.GetAccount(request.getId());
        if (account.passwdValidate(request.getPasswd())) {
            LoginResponse response = new LoginResponse();
            response.setId(request.getId());
            response.setTime(DateUtil.currentSeconds());
            response.setToken(jwtManager.generateToken(account.getId() , DateUtil.currentSeconds() + expire));
            response.setCode(LoginResponseCode.SUCCESS);
            response.setMessage("success");
            logger.info("login success, id: {}", request.getId());
            return response;
        } else {
            logger.warn("password verification failed, id = {}", request.getId());
            throw new SignNotMatch("password verification failed", request.getId());
        }
    }

    public LoginResponse LoginWithEmail(LoginWithEmailRequest request) {
        AccountProfile profile = accountProfileManager.GetAccountProfileWithEmail(request.getEmail());
        Account account = accountManager.GetAccount(profile.getUserId());
        if (account.passwdValidate(request.getPasswd())) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(account.getId());
            userInfo.setProfileId(profile.getId());
            LoginResponse response = new LoginResponse();
            response.setId(account.getId());
            response.setTime(DateUtil.currentSeconds());
            response.setCode(LoginResponseCode.SUCCESS);
            response.setMessage("success");
            response.setToken(jwtManager.generateToken(account.getId(), DateUtil.currentSeconds() + expire));
            logger.info("email login success, id = {}", account.getId());
            return response;
        }else {
            logger.warn("email-password verification failed, id = {}", account.getId());
            throw new SignNotMatch("email-password verification failed", account.getId());
        }
    }
}
