package com.inkorcloud.imlitejava.controller.message;

import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWTUtil;
import com.inkorcloud.imlitejava.service.exception.InvalidDataException;
import com.inkorcloud.imlitejava.service.exception.PermissionDeniedException;
import com.inkorcloud.imlitejava.util.KeyManager;
import com.inkorcloud.imlitejava.util.jwt.JWTManager;
import com.inkorcloud.imlitejava.util.jwt.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthChannelInterceptor implements ChannelInterceptor {
    private final JWTManager jwtManager;

    @Autowired
    public AuthChannelInterceptor(JWTManager jwtManager) {
        this.jwtManager = jwtManager;
    }

    @Override
    public Message<?> preSend(@NotNull Message<?> message, @NotNull MessageChannel channel) {
        var accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = StrUtil.removeAllPrefix(accessor.getFirstNativeHeader("Authorization"), "Bearer ");
            // Optionally, you can set the user information in the header for further processing
            if (!jwtManager.valiToken(token)) {
                log.warn("Invalid token: {}", token);
                throw new PermissionDeniedException("Invalid token");
            } else {
                // If the token is valid, extract user information and set it in the header
                UserInfo userInfo = jwtManager.parseJWT(token);
                if (userInfo != null) {
                    accessor.setUser(userInfo);
                    log.debug("User authenticated: {}", accessor.getUser());
                    log.debug("CONNECT, session = {}", accessor.getSessionId());
                } else {
                    log.warn("Failed to extract user info from token: {}", token);
                    throw new PermissionDeniedException("Failed to extract user info from token");
                }
            }
        }

        return message;
    }
}
