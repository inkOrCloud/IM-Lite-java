package com.inkorcloud.imlitejava.entity.session;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("session_members")
@Data
public class SessionMember {
    private Integer id;
    private Integer sessionId;
    private Integer memberId;
}
