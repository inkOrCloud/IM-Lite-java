package com.inkorcloud.imlitejava.controller.message;

import com.inkorcloud.imlitejava.controller.account.auth.UserInfoProvider;
import com.inkorcloud.imlitejava.entity.message.Message;
import com.inkorcloud.imlitejava.service.message.MessageManager;
import com.inkorcloud.imlitejava.util.jwt.UserInfo;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/message")
public class MessageController {
    private final MessageManager messageManager;

    @Autowired
    public MessageController(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    @MessageMapping("/chat/send")
    public void sendMessage(@NotNull @Payload Message message,
                            Principal principal) {
        messageManager.sendMessage(message, (UserInfo) principal);
    }

    @RequestMapping("get_messages_with_user_info")
    public List<Message> getMessagesWithUserInfo(@NotNull Long start,
                                                 @NotNull Long end,
                                                 @UserInfoProvider UserInfo userInfo) {
        if (start < 0 || end < 0 || start > end) {
            throw new IllegalArgumentException("Invalid time range");
        }
        return messageManager.getMessageWithUserInfo(userInfo, start, end);
    }

    @RequestMapping("get_messages_with_session_id")
    public List<Message> getMessagesWithSessionId(@UserInfoProvider UserInfo userInfo,
                                                  @NotNull Integer sessionId,
                                                  @NotNull Long start,
                                                  @NotNull Long end) {
        if (start < 0 || end < 0 || start > end) {
            throw new IllegalArgumentException("Invalid time range");
        }
        return messageManager.getMessageWithSessionID(sessionId, start, end, userInfo);
    }
}
