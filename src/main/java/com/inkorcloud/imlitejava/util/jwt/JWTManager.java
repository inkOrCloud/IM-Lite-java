package com.inkorcloud.imlitejava.util.jwt;

import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.inkorcloud.imlitejava.util.KeyManager;
import com.inkorcloud.imlitejava.entity.account.Account;
import com.inkorcloud.imlitejava.service.data.account.AccountManager;
import com.inkorcloud.imlitejava.entity.account.AccountProfile;
import com.inkorcloud.imlitejava.service.data.account.profile.AccountProfileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
@Component
public class JWTManager {
    AccountManager accountManager;
    AccountProfileManager accountProfileManager;
    KeyManager keyManager;
    public JWTManager(@Autowired KeyManager keyManager,
                      @Autowired AccountManager accountManager,
                      @Autowired AccountProfileManager accountProfileManager) {
        this.keyManager = keyManager;
        this.accountManager = accountManager;
        this.accountProfileManager = accountProfileManager;
    }
    public String generateToken(Integer userId, Long expire) {
        UserInfo userInfo = new UserInfo();
        Account account = accountManager.GetAccount(userId);
        AccountProfile profile = accountProfileManager.GetAccountProfile(userId);
        userInfo.setUserId(account.getId());
        userInfo.setProfileId(profile.getId());
        final JWT jwt = JWT.create()
                .setPayload("userInfo", userInfo)
                .setPayload("expire", expire)
                .setKey(keyManager.getJwtKey());
        return jwt.sign();
    }
    public boolean valiToken(String token) {
        return token != null && !token.isEmpty() && JWTUtil.verify(token, keyManager.getJwtKey());
    }
    public UserInfo parseJWT(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        return ((JSONObject)jwt.getPayload("userInfo")).toBean(UserInfo.class);
    }
}
