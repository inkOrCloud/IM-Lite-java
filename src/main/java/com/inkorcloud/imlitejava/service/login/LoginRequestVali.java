package com.inkorcloud.imlitejava.service.login;

import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.inkorcloud.imlitejava.service.login.exception.AlreadyAuthenticated;
import com.inkorcloud.imlitejava.service.login.exception.RequestTimeOut;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoginRequestVali {
    private final Logger logger = LoggerFactory.getLogger(LoginRequestVali.class);
    private final TimedCache<Integer, Boolean> loginTimedCache;

    public LoginRequestVali() {
        this.loginTimedCache = new TimedCache<>(DateUnit.SECOND.getMillis() * 301);
        this.loginTimedCache.schedulePrune(DateUnit.SECOND.getMillis());
    }

    @Order(10)
    @Before("execution(* com.inkorcloud.imlitejava.service.login.LoginService.Login(..)) ||" +
            "execution(* com.inkorcloud.imlitejava.service.login.LoginService.LoginWithEmail(..))")
    public void RepeatedVali(JoinPoint joinPoint) {
        Object request = joinPoint.getArgs()[0];
        Integer hash = request.hashCode();
        Boolean verify = loginTimedCache.get(hash);
        if(verify != null && verify) {
            logger.warn("repeated login, request: {}", request);
            throw new AlreadyAuthenticated("repeated login");
        }else{
            loginTimedCache.put(hash, true);
        }
    }

    @Order(20)
    @Before("execution(* com.inkorcloud.imlitejava.service.login.LoginService.*(..)) && args(TimeGetter,..))")
    public void RequestVali(JoinPoint joinPoint) {
        TimeGetter request = (TimeGetter) joinPoint.getArgs()[0];
        long diff = DateUtil.currentSeconds() - request.getTime();
        if (diff > 300 || diff < -30) {
            logger.warn("request time over 300 seconds");
            throw new RequestTimeOut("request time over 300 seconds");
        }
    }
}
