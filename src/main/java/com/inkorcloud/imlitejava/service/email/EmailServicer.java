package com.inkorcloud.imlitejava.service.email;

import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RandomUtil;
import com.inkorcloud.imlitejava.service.email.exception.FieldIsNotEmail;
import com.inkorcloud.imlitejava.service.email.exception.TooManyRequestsException;
import com.inkorcloud.imlitejava.service.email.exception.VerificationCodeNotRequestedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServicer {
    private final Logger logger = LoggerFactory.getLogger(EmailServicer.class);
    private final TimedCache<String, String> emailCodeCache;
    private final TimedCache<String, Boolean> emailQueryRate;
    private final String fromEmail;
    private final JavaMailSender mailSender;
    public EmailServicer(@Autowired JavaMailSender mailSender, @Value("${spring.mail.from}") String fromEmail) {
        this.mailSender = mailSender;
        this.emailCodeCache = new TimedCache<>(DateUnit.MINUTE.getMillis()*5);
        this.emailCodeCache.schedulePrune(DateUnit.SECOND.getMillis()*5);
        this.emailQueryRate = new TimedCache<>(DateUnit.SECOND.getMillis()*50);
        this.emailCodeCache.schedulePrune(DateUnit.SECOND.getMillis());
        this.fromEmail = fromEmail;
    }
    public EmailResponse CodeSend(String email) {
        if(!Validator.isEmail(email)) {
            logger.warn("field is not email, field value = {}", email);
            throw new FieldIsNotEmail("field is not email, field value = " + email);
        }
        Boolean IsQueried = emailQueryRate.get(email);
        if(IsQueried != null && IsQueried) {
            logger.warn("too many request, email = {}", email);
            throw new TooManyRequestsException("too many request, email = " + email);
        }
        String code = RandomUtil.randomString(6);
        emailCodeCache.put(email, code);
        emailQueryRate.put(email, true);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("IM-Lite Email Code");
        message.setText("code:" + code);
        mailSender.send(message);
        EmailResponse response = new EmailResponse();
        response.setEmail(email);
        response.setCode(EmailResponseCode.SUCCESS);
        response.setMessage("success");
        logger.info("send email code to {}", email);
        return response;
    }
    public String GetCode(String email) {
        String code = emailCodeCache.get(email);
        emailCodeCache.remove(email);
        if(code == null) {
            logger.warn("email not query code, email = {}", email);
            throw new VerificationCodeNotRequestedException(email, "verification code not requested");
        }
        return code;
    }
}
