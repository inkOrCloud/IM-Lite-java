package com.inkorcloud.imlitejava.service.data.account.friend;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inkorcloud.imlitejava.dao.mapper.FriendMapper;
import com.inkorcloud.imlitejava.entity.account.Friend;
import com.inkorcloud.imlitejava.entity.group.Group;
import com.inkorcloud.imlitejava.entity.session.Session;
import com.inkorcloud.imlitejava.entity.session.SessionMember;
import com.inkorcloud.imlitejava.entity.session.SessionType;
import com.inkorcloud.imlitejava.service.data.account.friend.exception.FriendsCountLimitExceededException;
import com.inkorcloud.imlitejava.service.exception.db.AlreadyExistsException;
import com.inkorcloud.imlitejava.service.exception.db.NotExistException;
import com.inkorcloud.imlitejava.service.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class FriendManager {
    private final FriendMapper friendMapper;
    private final SessionManager sessionManager;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public FriendManager(@Autowired FriendMapper friendMapper,
                         @Autowired SessionManager sessionManager) {
        this.friendMapper = friendMapper;
        this.sessionManager = sessionManager;
    }

    private int getFriendsCount(Integer accountId) {
        LambdaQueryWrapper<Friend> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Friend::getAccountId, accountId).isNull(Friend::getDeleteTime);
        return friendMapper.selectCount(wrapper).intValue();
    }

    @Transactional
    public Friend createFriend(Integer accountId, Integer friendId) {
        int count = getFriendsCount(accountId);
        if (count >= 500) {
            logger.warn("friends count exceeds 500, accountId={}", accountId);
            throw new FriendsCountLimitExceededException("friends count exceeds 500, accountId=" + accountId);
        }
        if (getFriendWithUserId(accountId, friendId) != null) {
            logger.warn("friend already exists, accountId={}, friendId={}", accountId, friendId);
            throw new AlreadyExistsException(
                    "friend already exists, accountId=" + accountId + ", friendId=" + friendId);
        }
        Friend friend = new Friend();
        friend.setAccountId(accountId);
        friend.setFriendId(friendId);
        friend.setCreateTime(DateUtil.currentSeconds());
        friend.setUpdateTime(DateUtil.currentSeconds());
        friend.setDeleteTime(null);
        friendMapper.insert(friend);
        if(sessionManager.getSessionWithUserIds(accountId, friendId) == null) {
            var session = new Session();
            session.setType(SessionType.PRIVATE);
            sessionManager.createSession(session);
            sessionManager.addMemberToSession(session.getId(), accountId);
            sessionManager.addMemberToSession(session.getId(), friendId);
        }
        return friend;
    }

    @Transactional
    public Friend removeFriend(Integer accountId, Integer friendId) {
        LambdaQueryWrapper<Friend> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Friend::getAccountId, accountId).eq(Friend::getFriendId, friendId);
        wrapper.isNull(Friend::getDeleteTime);
        Friend friend = friendMapper.selectOne(wrapper);
        if (friend == null) {
            logger.warn("friend remove fail: friend not exist, accountId={}, friendId={}", accountId, friendId);
            throw new NotExistException("friend not exist");
        }
        friend.setDeleteTime(DateUtil.currentSeconds());
        friend.setUpdateTime(DateUtil.currentSeconds());
        friendMapper.updateById(friend);
        if(getFriendWithUserId(friendId, accountId) == null) {
            var session = sessionManager.getSessionWithUserIds(accountId, friendId);
            sessionManager.deleteSession(session.getId());
        }
        return friend;
    }

    public Friend getFriend(Integer id) {
        LambdaQueryWrapper<Friend> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Friend::getId, id).isNull(Friend::getDeleteTime);
        Friend friend = friendMapper.selectOne(wrapper);
        if (friend == null) {
            logger.warn("friend get fail: friend not exist, accountId={}, friendId={}", id, id);
            throw new NotExistException("friend not exist");
        }
        return friend;
    }

    public List<Friend> getFriendsWithUserId(Integer accountId) {
        LambdaQueryWrapper<Friend> wrapper = new LambdaQueryWrapper<>();
        return friendMapper.selectList(wrapper.eq(Friend::getAccountId, accountId).isNull(Friend::getDeleteTime));
    }

    public Friend getFriendWithUserId(Integer accountId, Integer friendId) {
        LambdaQueryWrapper<Friend> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Friend::getAccountId, accountId).eq(Friend::getFriendId, friendId).isNull(Friend::getDeleteTime);
        return friendMapper.selectOne(wrapper);
    }
}
