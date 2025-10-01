package com.inkorcloud.imlitejava.entity.group;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@TableName("group_profiles")
@Data
public class GroupProfile {
    private Integer id;
    private Integer groupId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long avatarFileId;
    private String name;
    private String biography;
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;
    @TableField(fill = FieldFill.UPDATE)
    private Long updateTime;
    private Long deleteTime;
}
