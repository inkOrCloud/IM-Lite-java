package com.inkorcloud.imlitejava.service.data.account;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inkorcloud.imlitejava.entity.account.Account;
import com.inkorcloud.imlitejava.dao.mapper.AccountMapper;
import com.inkorcloud.imlitejava.service.exception.db.NotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
public class AccountManager {
    private final AccountMapper accountMapper;
    private final Logger logger = LoggerFactory.getLogger(AccountManager.class);
    public AccountManager(@Autowired AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Transactional
    public Account CreateAccount(Account account) {
        account.setCreateTime(DateUtil.currentSeconds());
        account.setUpdateTime(DateUtil.currentSeconds());
        account.setDeleteTime(null);
        accountMapper.insert(account);
        logger.info("Create account successful, id = {}", account.getId());
        return account;
    }

    @Transactional
    public void DeleteAccount(Account account) {
        account.setDeleteTime(DateUtil.currentSeconds());
        accountMapper.updateById(account);
        logger.info("Delete account successful, id = {}", account.getId());
    }

    public Account GetAccount(int accountId)  {
        LambdaQueryWrapper<Account> wrapper = new LambdaQueryWrapper<>();
        Account account = accountMapper.selectOne(wrapper.eq(Account::getId, accountId).isNull(Account::getDeleteTime));
        if(account == null) {
            logger.warn("Get account failed, account id {}", accountId);
            throw new NotExistException("account not found");
        }
        logger.info("Get account successful, id = {}", accountId);
        return account;
    }

    @Transactional
    public void UpdateAccount(Account account) {
        account.setUpdateTime(DateUtil.currentSeconds());
        accountMapper.updateById(account);
        logger.info("Update account successful, id = {}", account.getId());
    }
    public boolean AccountIsExist(int AccountId) {
        LambdaQueryWrapper<Account> wrapper = new LambdaQueryWrapper<>();
        Long n = accountMapper.selectCount(wrapper.eq(Account::getId, AccountId).isNull(Account::getDeleteTime));
        return n > 0;
    }
}
