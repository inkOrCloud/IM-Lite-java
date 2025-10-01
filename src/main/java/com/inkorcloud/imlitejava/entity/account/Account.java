package com.inkorcloud.imlitejava.entity.account;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Arrays;

@TableName("accounts")
@Data
public class Account {
    private Integer id;
    private byte[] passwdHash;
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;
    @TableField(fill = FieldFill.UPDATE)
    private Long updateTime;
    private Long deleteTime;
    private String salt;

    public boolean passwdValidate(String passwd) {
        var hash = DigestUtil.sha512(passwd + salt);
        return Arrays.equals(hash, passwdHash);
    }

    public void setPasswd(String passwd) {
        if (passwd == null || passwd.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
        for(char c : passwd.toCharArray()) {
            if (c <= 32 || c > 126) {
                throw new IllegalArgumentException("Password contains invalid characters");
            }
        }
        this.salt = RandomUtil.randomString(128);
        this.passwdHash = DigestUtil.sha512(passwd + salt);
    }
}
