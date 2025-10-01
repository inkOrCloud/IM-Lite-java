package com.inkorcloud.imlitejava.entity.account;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@TableName("friends")
@Data
public class Friend {
    private Integer id;
    private Integer accountId;
    private Integer friendId;
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;
    @TableField(fill = FieldFill.UPDATE)
    private Long updateTime;
    private Long deleteTime;
}
