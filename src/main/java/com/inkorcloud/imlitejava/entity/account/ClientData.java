package com.inkorcloud.imlitejava.entity.account;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("client_data")
@Data
public class ClientData {
    private Integer id;
    private Integer UserId;
    private String ClientData;
}
