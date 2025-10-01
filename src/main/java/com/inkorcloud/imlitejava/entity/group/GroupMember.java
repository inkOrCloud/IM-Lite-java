package com.inkorcloud.imlitejava.entity.group;

import com.baomidou.mybatisplus.annotation.*;
import com.inkorcloud.imlitejava.service.data.group.member.GroupMemberRole;
import lombok.Data;

@Data
@TableName("group_members")
public class GroupMember {
    private Integer id;
    private Integer groupId;
    private Integer memberId;
    private GroupMemberRole role;
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;
    @TableField(fill = FieldFill.UPDATE)
    private Long updateTime;
    private Long deleteTime;
}
