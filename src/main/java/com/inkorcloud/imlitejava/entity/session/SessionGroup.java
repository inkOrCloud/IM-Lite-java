package com.inkorcloud.imlitejava.entity.session;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("session_groups")
public class SessionGroup {
    private Integer id;
    private Integer sessionId;
    private Integer groupId;
}
