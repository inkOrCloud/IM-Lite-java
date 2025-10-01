package com.inkorcloud.imlitejava.entity.session;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("sessions")
@Data
public class Session {
    private Integer id;
    private SessionType type;
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;
    @TableField(fill = FieldFill.UPDATE)
    private Long updateTime;
    private Long deleteTime;
}
