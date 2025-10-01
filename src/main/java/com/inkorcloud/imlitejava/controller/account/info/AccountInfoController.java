package com.inkorcloud.imlitejava.controller.account.info;

import com.inkorcloud.imlitejava.controller.account.auth.UserInfoProvider;
import com.inkorcloud.imlitejava.entity.account.AccountProfile;
import com.inkorcloud.imlitejava.service.data.account.profile.AccountProfileManager;
import com.inkorcloud.imlitejava.util.jwt.UserInfo;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/account/info")
public class AccountInfoController {
    private final AccountProfileManager accountProfileManager;
    @SuppressWarnings("FieldCanBeLocal")
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public AccountInfoController(@Autowired AccountProfileManager accountProfileManager) {
        this.accountProfileManager = accountProfileManager;
    }

    @RequestMapping("get_profile")
    public AccountProfile GetAccountProfile(@NotNull Integer accountId) {
        return accountProfileManager.GetAccountProfileWithUserId(accountId);
    }

    @RequestMapping(value = "update_profile", method = RequestMethod.POST)
    public AccountProfile UpdateAccountProfile(@RequestBody @NotNull AccountProfile accountProfile,
                                               @UserInfoProvider UserInfo userInfo) {
        accountProfile.setId(userInfo.getUserId());
        accountProfileManager.UpdateAccountProfile(accountProfile);
        return accountProfileManager.GetAccountProfileWithUserId(accountProfile.getUserId());
    }
}
