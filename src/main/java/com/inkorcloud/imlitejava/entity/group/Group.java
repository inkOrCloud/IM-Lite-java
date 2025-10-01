package com.inkorcloud.imlitejava.entity.group;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@TableName("groups")
@Data
public class Group {
    private Integer id;
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;
    @TableField(fill = FieldFill.UPDATE)
    private Long updateTime;
    private Long deleteTime;
}
