package com.inkorcloud.imlitejava.service.data.group.member;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inkorcloud.imlitejava.dao.mapper.GroupMemberMapper;
import com.inkorcloud.imlitejava.entity.group.Group;
import com.inkorcloud.imlitejava.entity.group.GroupMember;
import com.inkorcloud.imlitejava.service.exception.PermissionDeniedException;
import com.inkorcloud.imlitejava.service.exception.db.AlreadyExistsException;
import com.inkorcloud.imlitejava.service.exception.db.NotExistException;
import com.inkorcloud.imlitejava.service.session.SessionManager;
import com.inkorcloud.imlitejava.util.jwt.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GroupMemberManager {
    private final GroupMemberMapper groupMemberMapper;
    private final SessionManager sessionManager;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public GroupMemberManager(@Autowired GroupMemberMapper groupMemberMapper,
                              @Autowired SessionManager sessionManager) {
        this.groupMemberMapper = groupMemberMapper;
        this.sessionManager = sessionManager;
    }

    @Transactional
    public GroupMember createGroupMember(GroupMember groupMember) {
        groupMember.setCreateTime(DateUtil.currentSeconds());
        groupMember.setUpdateTime(DateUtil.currentSeconds());
        groupMember.setDeleteTime(null);
        groupMemberMapper.insert(groupMember);
        var sessionId = sessionManager.getSessionIdWithGroupId(groupMember.getGroupId());
        sessionManager.addMemberToSession(sessionId, groupMember.getMemberId());
        return groupMember;
    }

    @Transactional
    public GroupMember addMemberToGroup(GroupMember groupMember, UserInfo userInfo) {
        var groupMemberWrapper = new LambdaQueryWrapper<GroupMember>();
        groupMemberWrapper.eq(GroupMember::getMemberId, userInfo.getUserId())
                .eq(GroupMember::getGroupId, groupMember.getGroupId()).isNull(GroupMember::getDeleteTime);
        var operator = groupMemberMapper.selectOne(groupMemberWrapper);
        var existingUserWrapper = new LambdaQueryWrapper<GroupMember>();
        existingUserWrapper.eq(GroupMember::getMemberId, groupMember.getMemberId())
                .eq(GroupMember::getGroupId, groupMember.getGroupId()).isNull(GroupMember::getDeleteTime);
        var existingUser = groupMemberMapper.selectOne(existingUserWrapper);
        if (existingUser != null) {
            logger.warn("add member to group failed, member already exist, user id: {}", userInfo.getUserId());
            throw new AlreadyExistsException("member already exist");
        }
        if (operator == null || operator.getRole().ordinal() < GroupMemberRole.ADMIN.ordinal()) {
            logger.warn("add member to group failed, permission denied, user id: {}", userInfo.getUserId());
            throw new PermissionDeniedException("permission denied");
        }
        if (groupMember.getRole().ordinal() > GroupMemberRole.MEMBER.ordinal()) {
            logger.warn("add member to group failed, permission denied, user id: {}", userInfo.getUserId());
            throw new PermissionDeniedException("permission denied");
        }
        return createGroupMember(groupMember);
    }

    @Transactional
    public GroupMember deleteGroupMember(Integer groupId, Integer memberId, UserInfo userInfo) {
        var memberWrapper = new LambdaQueryWrapper<GroupMember>();
        memberWrapper.eq(GroupMember::getMemberId, memberId);
        memberWrapper.eq(GroupMember::getGroupId, groupId);
        memberWrapper.isNull(GroupMember::getDeleteTime);
        var member = groupMemberMapper.selectOne(memberWrapper);
        LambdaQueryWrapper<GroupMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GroupMember::getMemberId, userInfo.getUserId()).isNull(GroupMember::getDeleteTime);
        var operator = groupMemberMapper.selectOne(wrapper);
        if (operator == null ||
                operator.getRole().ordinal() < GroupMemberRole.ADMIN.ordinal() ||
                operator.getRole().ordinal() <= member.getRole().ordinal()
        ) {
            logger.warn("delete group member failed, permission denied, user id:{}", userInfo.getUserId());
            throw new PermissionDeniedException("permission denied");
        }
        member.setDeleteTime(DateUtil.currentSeconds());
        member.setUpdateTime(DateUtil.currentSeconds());
        groupMemberMapper.updateById(member);
        var sessionId = sessionManager.getSessionIdWithGroupId(member.getGroupId());
        sessionManager.deleteMemberFromSession(sessionId, member.getMemberId());
        return member;
    }

    public GroupMember getGroupMember(Integer id) {
        LambdaQueryWrapper<GroupMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GroupMember::getId, id).isNull(GroupMember::getDeleteTime);
        GroupMember groupMember = groupMemberMapper.selectOne(wrapper);
        if (groupMember == null) {
            logger.warn("get group member failed, id:{}", id);
            throw new NotExistException("group member not exist");
        }
        return groupMember;
    }

    public List<GroupMember> getGroupMembersWithGroupId(Integer groupId) {
        LambdaQueryWrapper<GroupMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GroupMember::getGroupId, groupId).isNull(GroupMember::getDeleteTime);
        return groupMemberMapper.selectList(wrapper);
    }

    public List<GroupMember> getGroupMembersWithAccountId(Integer accountId) {
        LambdaQueryWrapper<GroupMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GroupMember::getGroupId, accountId).isNull(GroupMember::getDeleteTime);
        return groupMemberMapper.selectList(wrapper);
    }

    @Transactional
    public List<GroupMember> deleteGroupMembersWithGroupId(Integer groupId, UserInfo userInfo) {
        List<GroupMember> groupMembers = getGroupMembersWithGroupId(groupId);
        for (var member : groupMembers) {
            member.setDeleteTime(DateUtil.currentSeconds());
            member.setUpdateTime(DateUtil.currentSeconds());
            groupMemberMapper.updateById(member);
        }
        return groupMembers;
    }

    @Transactional
    public GroupMember updateGroupMember(GroupMember groupMember, UserInfo userInfo) {
        LambdaQueryWrapper<GroupMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GroupMember::getId, groupMember.getId()).isNull(GroupMember::getDeleteTime);
        GroupMember existingMember = groupMemberMapper.selectOne(wrapper);
        var operatorWrapper = new LambdaQueryWrapper<GroupMember>();
        operatorWrapper.eq(GroupMember::getMemberId, userInfo.getUserId())
                .eq(GroupMember::getGroupId, groupMember.getGroupId())
                .isNull(GroupMember::getDeleteTime);
        var operator = groupMemberMapper.selectOne(operatorWrapper);
        if (existingMember == null) {
            logger.warn("update group member failed, not exist, id:{}", groupMember.getId());
            throw new NotExistException("group member not exist");
        }
        if (operator == null) {
            logger.warn("update group member failed, permission denied, user id:{}", userInfo.getUserId());
            throw new PermissionDeniedException("permission denied");
        }
        if (operator.getRole().ordinal() <= groupMember.getRole().ordinal()) {
            logger.warn("update group member failed, permission denied, user id:{}", userInfo.getUserId());
            throw new PermissionDeniedException("permission denied");
        }
        if (groupMember.getRole().equals(GroupMemberRole.OWNER)) {
            LambdaQueryWrapper<GroupMember> ownerWrapper = new LambdaQueryWrapper<>();
            ownerWrapper.eq(GroupMember::getGroupId, groupMember.getGroupId())
                    .eq(GroupMember::getRole, GroupMemberRole.OWNER)
                    .isNull(GroupMember::getDeleteTime);
            GroupMember owner = groupMemberMapper.selectOne(ownerWrapper);
            if (owner != null && !owner.getId().equals(groupMember.getId())) {
                logger.warn("update group member failed, only one owner allowed, group id:{}", groupMember.getGroupId());
                throw new PermissionDeniedException("only one owner allowed");
            }
        }
        groupMember.setUpdateTime(DateUtil.currentSeconds());
        groupMemberMapper.updateById(groupMember);
        return groupMember;
    }

    public List<GroupMember> getGroupsWithGroupMember(Integer userId) {
        var wrapper = new LambdaQueryWrapper<GroupMember>();
        wrapper.eq(GroupMember::getMemberId, userId).isNull(GroupMember::getDeleteTime);
        return groupMemberMapper.selectList(wrapper);
    }

    public GroupMember getGroupMemberByGroupIdAndUserId(Integer groupId, Integer userId) {
        LambdaQueryWrapper<GroupMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GroupMember::getGroupId, groupId)
               .eq(GroupMember::getMemberId, userId)
               .isNull(GroupMember::getDeleteTime);
        return groupMemberMapper.selectOne(wrapper);
    }
}
