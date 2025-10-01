package com.inkorcloud.imlitejava.service.data.group;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inkorcloud.imlitejava.dao.mapper.GroupMapper;
import com.inkorcloud.imlitejava.dao.mapper.GroupMemberMapper;
import com.inkorcloud.imlitejava.entity.group.Group;
import com.inkorcloud.imlitejava.entity.group.GroupMember;
import com.inkorcloud.imlitejava.entity.session.Session;
import com.inkorcloud.imlitejava.entity.session.SessionType;
import com.inkorcloud.imlitejava.service.data.group.member.GroupMemberManager;
import com.inkorcloud.imlitejava.service.data.group.member.GroupMemberRole;
import com.inkorcloud.imlitejava.service.exception.PermissionDeniedException;
import com.inkorcloud.imlitejava.service.exception.db.NotExistException;
import com.inkorcloud.imlitejava.service.session.SessionManager;
import com.inkorcloud.imlitejava.util.jwt.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupManager {
    private final GroupMapper groupMapper;
    private final GroupMemberMapper groupMemberMapper;
    private final SessionManager sessionManager;
    private final GroupMemberManager groupMemberManager;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public GroupManager(GroupMapper groupMapper,
                        GroupMemberMapper groupMemberMapper,
                        SessionManager sessionManager,
                        GroupMemberManager groupMemberManager) {
        this.groupMapper = groupMapper;
        this.groupMemberMapper = groupMemberMapper;
        this.sessionManager = sessionManager;
        this.groupMemberManager = groupMemberManager;
    }

    public Group createGroup() {
        Group group = new Group();
        group.setCreateTime(DateUtil.currentSeconds());
        group.setUpdateTime(DateUtil.currentSeconds());
        group.setDeleteTime(null);
        groupMapper.insert(group);
        Session session = new Session();
        session.setType(SessionType.GROUP);
        sessionManager.createSessionWithGroupId(session, group.getId());
        return group;
    }

    public Group updateGroup(Group group) {
        group.setUpdateTime(DateUtil.currentSeconds());
        groupMapper.updateById(group);
        return group;
    }


    public Group deleteGroup(Group group, UserInfo userInfo) {
        LambdaQueryWrapper<GroupMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GroupMember::getMemberId, userInfo.getUserId())
                .eq(GroupMember::getGroupId, group.getId())
                .isNull(GroupMember::getDeleteTime);
        var groupMember = groupMemberMapper.selectOne(wrapper);
        if(groupMember == null || !groupMember.getRole().equals(GroupMemberRole.OWNER)) {
            logger.warn("group delete failed, not owner, user id: {}", userInfo.getUserId());
            throw new PermissionDeniedException("not owner");
        }
        var session = sessionManager.getSessionIdWithGroupId(group.getId());
        sessionManager.deleteSession(session);
        group.setDeleteTime(DateUtil.currentSeconds());
        groupMapper.updateById(group);
        return group;
    }

    public Group getGroup(Integer id) {
        LambdaQueryWrapper<Group> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Group::getId, id).isNull(Group::getDeleteTime);
        Group group = groupMapper.selectOne(wrapper);
        if(group == null) {
            logger.warn("group not found, id:{}", id);
            throw new NotExistException("group not found");
        }
        return group;
    }
}
