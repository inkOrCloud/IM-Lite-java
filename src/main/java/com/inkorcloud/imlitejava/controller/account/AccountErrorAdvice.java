package com.inkorcloud.imlitejava.controller.account;

import cn.hutool.core.date.DateUtil;
import com.inkorcloud.imlitejava.service.exception.PermissionDeniedException;
import com.inkorcloud.imlitejava.controller.account.exception.TokenExpire;
import com.inkorcloud.imlitejava.controller.account.auth.response.UnauthorizedResponse;
import com.inkorcloud.imlitejava.service.data.account.exception.PasswordFormatException;
import com.inkorcloud.imlitejava.service.login.LoginResponse;
import com.inkorcloud.imlitejava.service.login.LoginResponseCode;
import com.inkorcloud.imlitejava.service.login.exception.AlreadyAuthenticated;
import com.inkorcloud.imlitejava.service.login.exception.RequestTimeOut;
import com.inkorcloud.imlitejava.service.login.exception.SignNotMatch;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ControllerAdvice
@ResponseBody
public class AccountErrorAdvice {
    @ExceptionHandler
    public Object HandleLoginException(SignNotMatch e) {
        LoginResponse response = new LoginResponse();
        response.setCode(LoginResponseCode.SIGN_ERROR);
        response.setId(e.getId());
        response.setMessage(e.getMessage());
        response.setTime(DateUtil.current());
        return response;
    }

    @ExceptionHandler
    public Object HandleLoginException(RequestTimeOut e) {
        LoginResponse response = new LoginResponse();
        response.setCode(LoginResponseCode.REQUEST_TIMEOUT);
        response.setId(-1);
        response.setMessage(e.getMessage());
        response.setTime(DateUtil.current());
        return response;
    }

    @ExceptionHandler
    public Object HandleLoginException(AlreadyAuthenticated e) {
        LoginResponse response = new LoginResponse();
        response.setCode(LoginResponseCode.DUPLICATE_REQUEST);
        response.setId(e.getId());
        response.setMessage(e.getMessage());
        response.setTime(DateUtil.current());
        return response;
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public Object HandleTokenExpire(TokenExpire e) {
        UnauthorizedResponse response = new UnauthorizedResponse();
        response.setCode(0);
        response.setMessage(e.getMessage());
        return response;
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public Object HandleTokenExpire(PermissionDeniedException e) {
        UnauthorizedResponse response = new UnauthorizedResponse();
        response.setCode(1);
        response.setMessage(e.getMessage());
        return response;
    }

    @ExceptionHandler
    @ResponseStatus(code= HttpStatus.BAD_REQUEST)
    public Object HandlePasswordFormatException(PasswordFormatException e) {
        return Map.of("code", 400, "message", e.getMessage());
    }
}
