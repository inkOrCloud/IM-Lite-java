package com.inkorcloud.imlitejava.controller.email;

import com.inkorcloud.imlitejava.service.email.EmailResponse;
import com.inkorcloud.imlitejava.service.email.EmailResponseCode;
import com.inkorcloud.imlitejava.service.email.exception.FieldIsNotEmail;
import com.inkorcloud.imlitejava.service.email.exception.InvalidVerificationCodeException;
import com.inkorcloud.imlitejava.service.email.exception.TooManyRequestsException;
import com.inkorcloud.imlitejava.service.email.exception.VerificationCodeNotRequestedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@ResponseBody
public class EmailErrorAdvice {
    @ExceptionHandler
    public Object HandleEmailException(FieldIsNotEmail e) {
        EmailResponse response = new EmailResponse();
        response.setCode(EmailResponseCode.FIELD_IS_NOT_EMAIL);
        response.setEmail(e.getField());
        response.setMessage(e.getMessage());
        return response;
    }

    @ExceptionHandler
    public Object HandleEmailException(TooManyRequestsException e) {
        EmailResponse response = new EmailResponse();
        response.setCode(EmailResponseCode.TOO_MANY_REQUESTS);
        response.setMessage(e.getMessage());
        response.setEmail(e.getEmail());
        return response;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Object HandleEmailException(InvalidVerificationCodeException e) {
        Unauthorized response = new Unauthorized();
        response.setCode(UnauthorizedCode.CODE_VERIFICATION_FAILED.getCode());
        response.setMessage(e.getMessage());
        return response;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Object HandleEmailException(VerificationCodeNotRequestedException e) {
        Unauthorized response = new Unauthorized();
        response.setCode(UnauthorizedCode.NOT_REQUEST.getCode());
        response.setMessage(e.getMessage());
        return response;
    }
}
