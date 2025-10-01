package com.inkorcloud.imlitejava.service.security;

import com.inkorcloud.imlitejava.entity.account.Account;
import com.inkorcloud.imlitejava.service.data.account.AccountManager;
import com.inkorcloud.imlitejava.entity.account.AccountProfile;
import com.inkorcloud.imlitejava.service.data.account.profile.AccountProfileManager;
import com.inkorcloud.imlitejava.service.email.EmailServicer;
import com.inkorcloud.imlitejava.service.email.exception.VerificationCodeNotRequestedException;
import com.inkorcloud.imlitejava.service.security.exception.IncorrectPasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AccountProfileManager accountProfileManager;
    private final EmailServicer emailServicer;
    private final AccountManager accountManager;
    public SecurityService(@Autowired AccountManager accountManager,
                           @Autowired EmailServicer emailServicer,
                           @Autowired AccountProfileManager accountProfileManager) {
        this.accountManager = accountManager;
        this.emailServicer = emailServicer;
        this.accountProfileManager = accountProfileManager;
    }

    public ResetPasswdResponse ResetPasswd(ResetPasswdRequest request) {
        AccountProfile profile = accountProfileManager.GetAccountProfileWithEmail(request.getEmail());
        String code = emailServicer.GetCode(request.getEmail());
        if(code.equals(request.getCode())) {
            Account account = accountManager.GetAccount(profile.getUserId());
            account.setPasswd(request.getPassword());
            accountManager.UpdateAccount(account);
            ResetPasswdResponse response = new ResetPasswdResponse();
            response.setCode(ResetPasswdResponseCode.SUCCESS);
            response.setId(account.getId());
            response.setMessage("success");
            return response;
        }else {
            logger.warn("failed to reset passwd , wrong email code, email = {}", request.getEmail());
            throw new VerificationCodeNotRequestedException("wrong email code", request.getEmail());
        }
    }

    public ChangePasswdResponse ChangePasswd(ChangePasswdRequest request) {
        Account account = accountManager.GetAccount(request.getId());
        if(account.passwdValidate(request.getOldPass())) {
            account.setPasswd(request.getNewPass());
            accountManager.UpdateAccount(account);
            ChangePasswdResponse response = new ChangePasswdResponse();
            response.setId(request.getId());
            response.setCode(ChangePasswdResponseCode.SUCCESS);
            response.setMessage("success");
            return response;
        }else {
            logger.warn("failed to change passwd , wrong old pass, id = {}", request.getId());
            throw new IncorrectPasswordException(request.getId(), "wrong old pass");
        }
    }
}
