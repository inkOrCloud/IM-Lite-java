package com.inkorcloud.imlitejava.service.session;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inkorcloud.imlitejava.dao.mapper.SessionGroupMapper;
import com.inkorcloud.imlitejava.dao.mapper.SessionMapper;
import com.inkorcloud.imlitejava.dao.mapper.SessionMemberMapper;
import com.inkorcloud.imlitejava.entity.session.Session;
import com.inkorcloud.imlitejava.entity.session.SessionGroup;
import com.inkorcloud.imlitejava.entity.session.SessionMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Handler;

@Service
public class SessionManager {
    private final SessionMapper sessionMapper;
    private final SessionMemberMapper sessionMemberMapper;
    private final SessionGroupMapper sessionGroupMapper;

    public SessionManager(@Autowired SessionMapper sessionMapper,
                          @Autowired SessionMemberMapper sessionMemberMapper,
                          @Autowired SessionGroupMapper sessionGroupMapper) {
        this.sessionMapper = sessionMapper;
        this.sessionMemberMapper = sessionMemberMapper;
        this.sessionGroupMapper = sessionGroupMapper;
    }

    @Transactional
    public Session createSession(Session session) {
        session.setCreateTime(DateUtil.currentSeconds());
        session.setUpdateTime(null);
        session.setDeleteTime(null);
        sessionMapper.insert(session);
        return session;
    }

    @Transactional
    public void deleteSession(Integer sessionId) {
        Session session = sessionMapper.selectById(sessionId);
        if (session != null) {
            List<SessionMember> members = getSessionMembersWithSessionId(sessionId);
            for (SessionMember member : members) {
                sessionMemberMapper.deleteById(member);
            }
            session.setDeleteTime(DateUtil.currentSeconds());
            sessionMapper.updateById(session);
        }
    }

    public Session getSessionById(Integer sessionId) {
        Session session = sessionMapper.selectById(sessionId);
        if (session != null && session.getDeleteTime() == null) {
            return session;
        }
        return null; // Session not found or deleted
    }

    @Transactional
    public void addMemberToSession(Integer sessionId, Integer memberId) {
        SessionMember sessionMember = new SessionMember();
        sessionMember.setMemberId(memberId);
        sessionMember.setSessionId(sessionId);
        sessionMemberMapper.insert(sessionMember);
    }

    @Transactional
    public void deleteMemberFromSession(Integer sessionId, Integer userId) {
        LambdaQueryWrapper<SessionMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SessionMember::getSessionId, sessionId)
               .eq(SessionMember::getMemberId, userId);
        sessionMemberMapper.delete(wrapper);
    }

    public List<Session> getSessionsWithMemberId(Integer memberId) {
        LambdaQueryWrapper<SessionMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SessionMember::getMemberId, memberId);
        List<SessionMember> sessionMembers = sessionMemberMapper.selectList(wrapper);
        var sessionWrapper = new LambdaQueryWrapper<Session>();
        sessionWrapper.in(Session::getId, sessionMembers.stream().map(SessionMember::getSessionId).toList());
        sessionWrapper.isNull(Session::getDeleteTime);
        return sessionMapper.selectList(sessionWrapper);
    }

    public List<SessionMember> getSessionMembersWithUserId(Integer userId) {
        LambdaQueryWrapper<SessionMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SessionMember::getMemberId, userId);
        return sessionMemberMapper.selectList(wrapper);
    }

    public List<SessionMember> getSessionMembersWithSessionId(Integer sessionId) {
        LambdaQueryWrapper<SessionMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SessionMember::getSessionId, sessionId);
        return sessionMemberMapper.selectList(wrapper);
    }

    public Session getSessionWithUserIds(Integer user1, Integer user2) {
        LambdaQueryWrapper<SessionMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SessionMember::getMemberId, List.of(user1, user2));
        List<SessionMember> sessionMembers = sessionMemberMapper.selectList(wrapper);
        var sessionSet = new HashSet<Integer>();
        if (sessionMembers.size() < 2) {
            return null; // Not enough members to form a session
        }
        var sessionId = -1;
        for (SessionMember member : sessionMembers) {
            if(sessionSet.contains(member.getSessionId())) {
                sessionId = member.getSessionId();
                break;
            }
            sessionSet.add(member.getSessionId());
        }
        if (sessionId == -1) {
            return null; // No common session found
        };
        return getSessionById(sessionId);
    }

    @Transactional
    public Session createSessionWithGroupId(Session session, Integer groupId) {
        this.createSession(session);
        var sessionGroup = new SessionGroup();
        sessionGroup.setSessionId(session.getId());
        sessionGroup.setGroupId(groupId);
        sessionGroupMapper.insert(sessionGroup);
        return session;
    }

    public Integer getSessionIdWithGroupId(Integer groupId) {
        LambdaQueryWrapper<SessionGroup> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SessionGroup::getGroupId, groupId);
        SessionGroup sessionGroup = sessionGroupMapper.selectOne(wrapper);
        if (sessionGroup != null) {
            return sessionGroup.getSessionId();
        }
        return null;
    }

    public Integer getGroupIdWithSessionId(Integer sessionId) {
        LambdaQueryWrapper<SessionGroup> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SessionGroup::getSessionId, sessionId);
        SessionGroup sessionGroup = sessionGroupMapper.selectOne(wrapper);
        if (sessionGroup != null) {
            return sessionGroup.getGroupId();
        }
        return null;
    }

}
