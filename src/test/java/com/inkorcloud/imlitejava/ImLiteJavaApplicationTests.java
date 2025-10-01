package com.inkorcloud.imlitejava;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.inkorcloud.imlitejava.entity.account.Account;
import com.inkorcloud.imlitejava.entity.account.AccountProfile;
import com.inkorcloud.imlitejava.service.data.account.AccountManager;
import com.inkorcloud.imlitejava.service.data.account.profile.AccountProfileManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Primary;

@SpringBootTest
class ImLiteJavaApplicationTests {

    @Autowired
    public ImLiteJavaApplicationTests(AccountManager accountManager,
                                      AccountProfileManager accountProfileManager) {
    }


    @Test
    void contextLoads() {
    }

}
