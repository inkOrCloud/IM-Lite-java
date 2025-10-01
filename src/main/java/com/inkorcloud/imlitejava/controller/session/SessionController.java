package com.inkorcloud.imlitejava.controller.session;

import ch.qos.logback.core.net.AutoFlushingObjectWriter;
import com.inkorcloud.imlitejava.controller.account.auth.UserInfoProvider;
import com.inkorcloud.imlitejava.entity.session.Session;
import com.inkorcloud.imlitejava.entity.session.SessionMember;
import com.inkorcloud.imlitejava.service.session.SessionManager;
import com.inkorcloud.imlitejava.util.jwt.UserInfo;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/session")
public class SessionController {
    private final SessionManager sessionManager;
    @Autowired
    public SessionController(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @RequestMapping("get_session_id_with_user_id")
    public Integer getSessionIdWithUserId(@UserInfoProvider UserInfo userInfo, @NotNull Integer user) {
        return this.sessionManager.getSessionWithUserIds(userInfo.getUserId(), user).getId();
    }

    @RequestMapping("get_session_id_with_group_id")
    public Integer getSessionIdWithGroupId(@NotNull Integer groupId) {
        return this.sessionManager.getSessionIdWithGroupId(groupId);
    }

    @RequestMapping("get_session_ids_with_user_info")
    public List<Integer> getSessionIdsWithUserInfo(@UserInfoProvider UserInfo userInfo) {
        var members = this.sessionManager.getSessionMembersWithUserId(userInfo.getUserId());
        return members.stream().map(SessionMember::getSessionId).toList();
    }

    @RequestMapping("get_sessions_with_user_info")
    public List<Session> getSessionsWithUserInfo(@UserInfoProvider UserInfo userInfo) {
        return  this.sessionManager.getSessionsWithMemberId(userInfo.getUserId());
    }

    @RequestMapping("get_user_ids_with_session_id")
    public List<Integer> getUserIdsWithSessionId(@NotNull Integer sessionId) {
        var members = this.sessionManager.getSessionMembersWithSessionId(sessionId);
        return members.stream().map(SessionMember::getMemberId).toList();
    }

    @RequestMapping("get_group_id_with_session_id")
    public Integer getGroupIdWithSessionId(@NotNull Integer sessionId) {
        return this.sessionManager.getGroupIdWithSessionId(sessionId);
    }
}
