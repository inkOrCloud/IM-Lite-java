package com.inkorcloud.imlitejava.service.data.group.profile;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inkorcloud.imlitejava.dao.mapper.GroupMemberMapper;
import com.inkorcloud.imlitejava.dao.mapper.GroupProfileMapper;
import com.inkorcloud.imlitejava.entity.group.GroupMember;
import com.inkorcloud.imlitejava.entity.group.GroupProfile;
import com.inkorcloud.imlitejava.service.data.group.member.GroupMemberRole;
import com.inkorcloud.imlitejava.service.exception.PermissionDeniedException;
import com.inkorcloud.imlitejava.service.exception.db.NotExistException;
import com.inkorcloud.imlitejava.util.jwt.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupProfileManager {
    private final Logger logger = LoggerFactory.getLogger(GroupProfileManager.class);
    private final GroupProfileMapper groupProfileMapper;
    private final GroupMemberMapper groupMemberMapper;
    public GroupProfileManager(@Autowired GroupProfileMapper groupProfileMapper,
                               @Autowired GroupMemberMapper groupMemberMapper) {
        this.groupProfileMapper = groupProfileMapper;
        this.groupMemberMapper = groupMemberMapper;
    }

    public GroupProfile createGroupProfile(GroupProfile profile) {
        profile.setCreateTime(DateUtil.currentSeconds());
        profile.setUpdateTime(DateUtil.currentSeconds());
        profile.setDeleteTime(null);
        groupProfileMapper.insert(profile);
        return profile;
    }

    public GroupProfile updateGroupProfile(GroupProfile profile, UserInfo userInfo) {
        Integer userId = userInfo.getUserId();
        LambdaQueryWrapper<GroupMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GroupMember::getMemberId, userId)
                .eq(GroupMember::getGroupId, profile.getGroupId())
                .isNull(GroupMember::getDeleteTime);
        GroupMember groupMember = groupMemberMapper.selectOne(wrapper);
        if (groupMember == null ||
                !(groupMember.getRole().equals(GroupMemberRole.OWNER) ||
                        groupMember.getRole().equals(GroupMemberRole.ADMIN))) {
            logger.warn("update group profile failed, not owner or admin, user id: {}", userId);
            throw new PermissionDeniedException("not owner or admin");
        }
        profile.setUpdateTime(DateUtil.currentSeconds());
        groupProfileMapper.updateById(profile);
        logger.info("group profile update, group id = {}", profile.getGroupId());
        return profile;
    }

    public GroupProfile getGroupProfileWithGroupId(Integer groupId) {
        LambdaQueryWrapper<GroupProfile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GroupProfile::getGroupId, groupId);
        GroupProfile profile = groupProfileMapper.selectOne(wrapper);
        if(profile == null) {
            logger.warn("group profile not found, groupId: {}", groupId);
            throw new NotExistException("group profile not found");
        }
        return profile;
    }

    public GroupProfile getGroupProfile(Integer id) {
        GroupProfile profile = groupProfileMapper.selectById(id);
        if(profile == null) {
            logger.warn("group profile not found, id: {}", id);
            throw new NotExistException("group profile not found");
        }
        return profile;
    }

    public GroupProfile deleteGroupProfile(GroupProfile profile, UserInfo userInfo) {
        profile.setDeleteTime(DateUtil.currentSeconds());
        this.updateGroupProfile(profile, userInfo);
        return profile;
    }
}
