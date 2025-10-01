package com.inkorcloud.imlitejava.controller.account.auth;

import cn.hutool.core.util.StrUtil;
import com.inkorcloud.imlitejava.controller.account.exception.TokenExpire;
import com.inkorcloud.imlitejava.util.jwt.JWTManager;
import com.inkorcloud.imlitejava.util.jwt.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class JwtAuthentication {
    private final JWTManager jwtManager;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public JwtAuthentication(@Autowired JWTManager jwtManager) {
        this.jwtManager = jwtManager;
    }

    @Pointcut("within(com.inkorcloud.imlitejava.controller.account.login.LoginController) ||" +
            "within(com.inkorcloud.imlitejava.controller.account.register.RegisterController) ||" +
            "within(com.inkorcloud.imlitejava.controller.email..*) ||" +
            "execution(* com.inkorcloud.imlitejava.controller.account.SecurityController.ResetPasswd(..)) ||" +
            "execution(* com.inkorcloud.imlitejava.controller.message.MessageController.sendMessage(..))")
    private void whiteListPointCut() {}

    @Before("within(com.inkorcloud.imlitejava.controller..*) && " +
            "@within(org.springframework.web.bind.annotation.RestController) &&" +
            "!whiteListPointCut()")
    public void JWTVali() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        if (requestAttributes != null) {
            request = requestAttributes.getRequest();
        }
        String token = null;
        if (request != null) {
            token = request.getHeader("Authorization");
        }
        logger.debug("client token = {}", token);
        if (token != null) {
            token = StrUtil.removePrefix(token, "Bearer ");
            if(!jwtManager.valiToken(token)) {
                logger.warn("token validate failed, token:{}", token);
                throw new TokenExpire("token validate failed");
            }
        }else {
            logger.warn("token is null");
            throw new TokenExpire("token is null");
        }
    }
}
