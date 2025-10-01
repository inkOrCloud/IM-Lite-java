package com.inkorcloud.imlitejava.controller.account.auth;

import cn.hutool.core.util.StrUtil;
import com.inkorcloud.imlitejava.controller.account.exception.TokenExpire;
import com.inkorcloud.imlitejava.util.jwt.JWTManager;
import com.inkorcloud.imlitejava.util.jwt.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class UserInfoArgumentResolver implements HandlerMethodArgumentResolver {
    private final JWTManager jwtManager;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public UserInfoArgumentResolver(@Autowired JWTManager jwtManager) {
        this.jwtManager = jwtManager;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserInfo.class) &&
                parameter.hasParameterAnnotation(UserInfoProvider.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        String token = webRequest.getHeader("Authorization");
        if(token == null) {
            logger.warn("token is null");
            throw new TokenExpire("token is null");
        }
        token = StrUtil.removePrefix(token, "Bearer ");
        return jwtManager.parseJWT(token);
    }
}
