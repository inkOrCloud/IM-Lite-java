package com.inkorcloud.imlitejava.controller.group;

import com.inkorcloud.imlitejava.controller.account.auth.UserInfoProvider;
import com.inkorcloud.imlitejava.entity.group.Group;
import com.inkorcloud.imlitejava.entity.group.GroupMember;
import com.inkorcloud.imlitejava.entity.group.GroupProfile;
import com.inkorcloud.imlitejava.service.data.group.GroupManager;
import com.inkorcloud.imlitejava.service.data.group.member.GroupMemberManager;
import com.inkorcloud.imlitejava.service.data.group.member.GroupMemberRole;
import com.inkorcloud.imlitejava.service.data.group.profile.GroupProfileManager;
import com.inkorcloud.imlitejava.util.jwt.UserInfo;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/group")
public class GroupController {
    private final GroupManager groupManager;
    private final GroupMemberManager groupMemberManager;
    private final GroupProfileManager groupProfileManager;

    public GroupController(@Autowired GroupManager groupManager,
                           @Autowired GroupMemberManager groupMemberManager,
                           @Autowired GroupProfileManager groupProfileManager) {
        this.groupManager = groupManager;
        this.groupMemberManager = groupMemberManager;
        this.groupProfileManager = groupProfileManager;
    }

    @RequestMapping("create_group")
    public Group createGroup(@UserInfoProvider UserInfo userInfo, @NotNull String name) {
        Group group = groupManager.createGroup();
        GroupProfile profile = new GroupProfile();
        profile.setGroupId(group.getId());
        profile.setName(name);
        groupProfileManager.createGroupProfile(profile);
        GroupMember member = new GroupMember();
        member.setGroupId(group.getId());
        member.setMemberId(userInfo.getUserId());
        member.setRole(GroupMemberRole.OWNER);
        groupMemberManager.createGroupMember(member);
        return group;
    }

    @RequestMapping("get_group")
    public Group getGroup(@NotNull Integer id) {
        return groupManager.getGroup(id);
    }

    @RequestMapping("get_group_profile")
    public GroupProfile getGroupProfile(@NotNull Integer groupId) {
        return groupProfileManager.getGroupProfileWithGroupId(groupId);
    }

    @RequestMapping(value = "update_group_profile", method = RequestMethod.POST)
    public GroupProfile updateGroupProfile(@UserInfoProvider UserInfo userInfo,
                                           @RequestBody @NotNull GroupProfile groupProfile) {
        return groupProfileManager.updateGroupProfile(groupProfile, userInfo);
    }

    @RequestMapping("delete_group")
    public Group deleteGroup(@UserInfoProvider UserInfo userInfo,
                                           @NotNull Integer id) {
        Group group = groupManager.getGroup(id);
        groupManager.deleteGroup(group, userInfo);
        GroupProfile groupProfile = groupProfileManager.getGroupProfileWithGroupId(id);
        groupProfileManager.deleteGroupProfile(groupProfile, userInfo);
        groupMemberManager.deleteGroupMembersWithGroupId(id, userInfo);
        return group;
    }

    @RequestMapping("get_group_members")
    public List<GroupMember> getGroupMembers(@NotNull Integer groupId, @UserInfoProvider UserInfo userInfo) {
        return groupMemberManager.getGroupMembersWithGroupId(groupId);
    }

    @RequestMapping(value = "add_group_member", method = RequestMethod.POST)
    public GroupMember addGroupMember(@UserInfoProvider UserInfo userInfo,
                                           @NotNull @RequestBody GroupMember groupMember) {
        return groupMemberManager.addMemberToGroup(groupMember, userInfo);
    }

    @RequestMapping(value = "update_group_member", method = RequestMethod.POST)
    public GroupMember updateGroupMember(@UserInfoProvider UserInfo userInfo,
                                           @NotNull @RequestBody GroupMember groupMember) {
        return groupMemberManager.updateGroupMember(groupMember, userInfo);
    }

    @RequestMapping(value = "delete_group_member")
    public GroupMember deleteGroupMember(@UserInfoProvider UserInfo userInfo,
                                           @NotNull Integer groupId, @NotNull Integer memberId) {
        return groupMemberManager.deleteGroupMember(groupId, memberId, userInfo);
    }

    @RequestMapping("get_group_members_with_user_id")
    public List<GroupMember> getGroupMembersWithUserId(@UserInfoProvider UserInfo userInfo) {
        return this.groupMemberManager.getGroupsWithGroupMember(userInfo.getUserId());
    }

    @RequestMapping("get_group_member_with_group_id_and_user_id")
    public GroupMember getGroupMemberWithGroupIdAndUserId(Integer groupId, Integer userId) {
        return this.groupMemberManager.getGroupMemberByGroupIdAndUserId(groupId, userId);
    }
}
