package com.inkorcloud.imlitejava.service.data.account.profile;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inkorcloud.imlitejava.dao.mapper.AccountProfileMapper;
import com.inkorcloud.imlitejava.entity.account.AccountProfile;
import com.inkorcloud.imlitejava.service.exception.OperationException;
import com.inkorcloud.imlitejava.service.exception.db.AlreadyExistsException;
import com.inkorcloud.imlitejava.service.exception.db.NotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountProfileManager {
    private final AccountProfileMapper accountProfileMapper;
    private final Logger logger = LoggerFactory.getLogger(AccountProfileManager.class);

    public AccountProfileManager(@Autowired AccountProfileMapper accountProfileMapper) {
        this.accountProfileMapper = accountProfileMapper;
    }

    @Transactional
    public AccountProfile CreateAccountProfile(AccountProfile accountProfile) {
        if(this.AccountProfileIsExistWithEmail(accountProfile.getEmail())) {
            logger.warn("Account profile with email {} already exists", accountProfile.getEmail());
            throw new AlreadyExistsException("Account profile with email " + accountProfile.getEmail()
                    + " already exists");
        }
        if(accountProfile.getUserId() == null) {
            logger.error("create user profile fail: user id is null");
            throw new OperationException("user id is null");
        }
        if(this.AccountProfileIsExist(accountProfile.getUserId())) {
            logger.warn("Account profile with id {} already exists", accountProfile.getUserId());
            throw new AlreadyExistsException("Account profile with id " + accountProfile.getUserId()
                    + " already exists");
        }
        accountProfile.setCreateTime(DateUtil.currentSeconds());
        accountProfile.setUpdateTime(DateUtil.currentSeconds());
        accountProfile.setDeleteTime(null);
        accountProfileMapper.insert(accountProfile);
        logger.info("create account profile, id = {}", accountProfile.getId());
        return accountProfile;
    }

    @Transactional
    public void DeleteAccountProfile(AccountProfile accountProfile) {
        accountProfile.setDeleteTime(DateUtil.currentSeconds());
        accountProfile.setEmail(accountProfile.getEmail()+"-delete");
        accountProfileMapper.updateById(accountProfile);
        logger.info("account profile delete, id = {}", accountProfile.getId());
    }

    public AccountProfile GetAccountProfileWithUserId(int accountId) throws NotExistException {
        LambdaQueryWrapper<AccountProfile> wrapper = new LambdaQueryWrapper<>();
        AccountProfile profile = accountProfileMapper.selectOne(
                wrapper.eq(AccountProfile::getUserId, accountId).isNull(AccountProfile::getDeleteTime));
        if(profile == null) {
            logger.warn("Account profile with user id {} does not exist", accountId);
            throw new NotExistException("Account profile with user id " + accountId + " does not exist");
        }
        logger.info("query account profile, user id = {}", profile.getUserId());
        return profile;
    }

    @Transactional
    public void UpdateAccountProfile(AccountProfile accountProfile) {
        accountProfile.setUpdateTime(DateUtil.currentSeconds());
        accountProfileMapper.updateById(accountProfile);
        logger.info("account profile update, id = {}", accountProfile.getId());
    }

    public boolean AccountProfileIsExist(Integer accountId) {
        LambdaQueryWrapper<AccountProfile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AccountProfile::getUserId, accountId).isNull(AccountProfile::getDeleteTime);
        Long n = accountProfileMapper.selectCount(wrapper);
        return n > 0;
    }

    public AccountProfile GetAccountProfileWithEmail(String email) throws NotExistException {
        LambdaQueryWrapper<AccountProfile> wrapper = new LambdaQueryWrapper<>();
        AccountProfile profile = accountProfileMapper.selectOne(
                wrapper.eq(AccountProfile::getEmail, email).isNull(AccountProfile::getDeleteTime));
        if(profile == null) {
            logger.warn("Account profile with email {} does not exist", email);
            throw new NotExistException("Account profile with email " + email + " does not exist");
        }
        logger.info("query account profile with email, id = {}", profile.getId());
        return profile;
    }

    public boolean AccountProfileIsExistWithEmail(String email) {
        LambdaQueryWrapper<AccountProfile> wrapper = new LambdaQueryWrapper<>();
        Long n = accountProfileMapper.selectCount(
                wrapper.eq(AccountProfile::getEmail, email).isNull(AccountProfile::getDeleteTime));
        return n > 0;
    }

    public AccountProfile GetAccountProfile(Integer id) {
        LambdaQueryWrapper<AccountProfile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AccountProfile::getId, id).isNull(AccountProfile::getDeleteTime);
        AccountProfile profile = accountProfileMapper.selectOne(wrapper);
        if(profile == null) {
            logger.warn("Account profile with id {} does not exist", id);
            throw new NotExistException("Account profile with id " + id + " does not exist");
        }
        logger.info("query account profile, id = {}", profile.getId());
        return profile;
    }
}
