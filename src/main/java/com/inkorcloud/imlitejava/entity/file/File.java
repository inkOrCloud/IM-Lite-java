package com.inkorcloud.imlitejava.entity.file;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@TableName("files")
@Data
public class File {
    @TableId(type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String name;
    private String path;
    private Long size;
    private String hash;
    private Integer uploaderId;
    private String token;
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;
    @TableField(fill = FieldFill.UPDATE)
    private Long updateTime;
    private Long deleteTime;
}
