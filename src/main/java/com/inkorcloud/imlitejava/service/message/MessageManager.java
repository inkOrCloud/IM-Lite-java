package com.inkorcloud.imlitejava.service.message;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inkorcloud.imlitejava.dao.mapper.MessageMapper;
import com.inkorcloud.imlitejava.dao.mapper.SessionMemberMapper;
import com.inkorcloud.imlitejava.entity.message.Message;
import com.inkorcloud.imlitejava.entity.message.MessageType;
import com.inkorcloud.imlitejava.entity.session.Session;
import com.inkorcloud.imlitejava.entity.session.SessionMember;
import com.inkorcloud.imlitejava.service.exception.PermissionDeniedException;
import com.inkorcloud.imlitejava.service.exception.db.NotExistException;
import com.inkorcloud.imlitejava.service.session.SessionManager;
import com.inkorcloud.imlitejava.util.jwt.UserInfo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageManager {
    private final MessageMapper messageMapper;
    private final SessionManager sessionManager;
    private final SessionMemberMapper sessionMemberMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(MessageManager.class);

    @Autowired
    public MessageManager(MessageMapper messageMapper,
                          SessionManager sessionManager,
                          SessionMemberMapper sessionMemberMapper,
                          SimpMessagingTemplate simpMessagingTemplate) {
        this.messageMapper = messageMapper;
        this.sessionManager = sessionManager;
        this.sessionMemberMapper = sessionMemberMapper;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public List<Message> getMessageWithUserInfo(UserInfo userInfo, Long start, Long end) {
        var sessionIds = sessionManager.getSessionsWithMemberId(userInfo.getUserId())
                .stream()
                .map(Session::getId)
                .toList();
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Message::getSessionId, sessionIds);
        wrapper.between(Message::getCreateTime, start, end);
        wrapper.isNull(Message::getDeleteTime);
        logger.debug("get messages for user {}, session ids: {}, time range: {} - {}",
                userInfo.getUserId(), sessionIds, start, end);
        return messageMapper.selectList(wrapper);
    }

    public List<Message> getMessageWithSessionID(Integer sessionId, Long start, Long end, UserInfo userInfo) {
        var sessionMemberWrapper = new LambdaQueryWrapper<SessionMember>();
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        sessionMemberWrapper.eq(SessionMember::getSessionId, sessionId)
                .eq(SessionMember::getMemberId, userInfo.getUserId());
        if (sessionMemberMapper.selectCount(sessionMemberWrapper) == 0) {
            logger.warn("get messages failed, user {} tried to access messages in session {} without permission",
                    userInfo.getUserId(), sessionId);
            throw
                    new PermissionDeniedException("You are not a member of this session");
        }
        wrapper.eq(Message::getSessionId, sessionId);
        wrapper.between(Message::getCreateTime, start, end);
        return messageMapper.selectList(wrapper);
    }

    @Transactional
    public Message createMessage(Message message, UserInfo userInfo) {
        var sessionMemberWrapper = new LambdaQueryWrapper<SessionMember>();
        sessionMemberWrapper.eq(SessionMember::getSessionId, message.getSessionId())
                .eq(SessionMember::getMemberId, userInfo.getUserId());
        if (sessionMemberMapper.selectCount(sessionMemberWrapper) == 0) {
            logger.warn("create message failed, user {} tried to create message in session {} without permission",
                    userInfo.getUserId(), message.getSessionId());
            throw new PermissionDeniedException("You are not a member of this session");
        }
        message.setCreateTime(DateUtil.currentSeconds());
        message.setUserId(userInfo.getUserId());
        messageMapper.insert(message);
        return message;
    }

    @Transactional
    public void deleteMessage(Long messageId, UserInfo userInfo) {
        var message = messageMapper.selectById(messageId);
        if (message == null) {
            logger.warn("delete message failed, message with id {} not found", messageId);
            throw new NotExistException("Message not found");
        }
        if (!message.getUserId().equals(userInfo.getUserId())) {
            logger.warn("delete message failed, message sender is not this user, user id = {}, message id = {}",
                    userInfo.getUserId(), message.getId());
            throw new PermissionDeniedException("You can only delete your own messages");
        }
        var sessionMemberWrapper = new LambdaQueryWrapper<SessionMember>();
        sessionMemberWrapper.eq(SessionMember::getSessionId, message.getSessionId())
                .eq(SessionMember::getMemberId, userInfo.getUserId());
        if (sessionMemberMapper.selectCount(sessionMemberWrapper) == 0) {
            logger.warn("delete message failed, user {} tried to delete message in session {} without permission",
                    userInfo.getUserId(), message.getSessionId());
            throw new PermissionDeniedException("You are not a member of this session");
        }
        if (DateUtil.currentSeconds() - message.getCreateTime() > 180) {
            logger.warn("delete message failed, message with id {} is too old to delete", messageId);
            throw new PermissionDeniedException("You can only delete messages within 3 minutes of creation");
        }
        message.setDeleteTime(DateUtil.currentSeconds());
        messageMapper.updateById(message);
    }

    @Transactional
    public void sendMessage(Message message, UserInfo userInfo) {
        if (message.getType().equals(MessageType.OPERATION_DELETE)) {
            this.deleteMessage(Long.parseLong(message.getContent()), userInfo);
        } else {
            this.createMessage(message, userInfo);
        }
        simpMessagingTemplate.convertAndSend(
                "/topic/session." + message.getSessionId(),
                message
        );
    }
}
