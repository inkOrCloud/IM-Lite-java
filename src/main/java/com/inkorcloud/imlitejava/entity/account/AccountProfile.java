package com.inkorcloud.imlitejava.entity.account;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("account_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountProfile {
    private Integer id;
    private Integer userId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long avatarFileId;
    private String name;
    private String biography;
    private String email;
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;
    @TableField(fill = FieldFill.UPDATE)
    private Long updateTime;
    private Long deleteTime;
}
