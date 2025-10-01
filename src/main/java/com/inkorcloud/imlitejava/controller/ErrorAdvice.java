package com.inkorcloud.imlitejava.controller;

import com.inkorcloud.imlitejava.service.data.account.exception.PasswordFormatException;
import com.inkorcloud.imlitejava.service.email.exception.InvalidVerificationCodeException;
import com.inkorcloud.imlitejava.service.email.exception.VerificationCodeNotRequestedException;
import com.inkorcloud.imlitejava.service.exception.ArgsInvalidException;
import com.inkorcloud.imlitejava.service.exception.db.AlreadyExistsException;
import com.inkorcloud.imlitejava.service.exception.db.NotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ControllerAdvice
@ResponseBody
public class ErrorAdvice {
    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Object HandelArgsInvalidException(MethodArgumentNotValidException e) {
        return Map.of("code", 400, "message", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Object HandelArgsInvalidException(ArgsInvalidException e) {
        return Map.of("code", 400, "message", e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(code= HttpStatus.CONFLICT)
    public Map<String, Object> HandleConflict(AlreadyExistsException e) {
        return Map.of("code", 409, "message", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(code= HttpStatus.NOT_FOUND)
    public Map<String, Object> HandleNotExist(NotExistException e) {
        return Map.of("code", 404, "message", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
    public Map<String, Object> HandleInvalidVerificationCodeException(InvalidVerificationCodeException e) {
        return Map.of("code", 422, "message", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
    public Map<String, Object> HandleVerificationCodeNotRequestedException(VerificationCodeNotRequestedException e) {
        return Map.of("code", 422, "message", e.getMessage());
    }
}
