package com.inkorcloud.imlitejava.service.data.account.clientdata;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inkorcloud.imlitejava.dao.mapper.ClientDataMapper;
import com.inkorcloud.imlitejava.entity.account.ClientData;
import com.inkorcloud.imlitejava.service.exception.db.NotExistException;
import com.inkorcloud.imlitejava.util.jwt.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.ClassEditor;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ClientDataManager {
    private final ClientDataMapper clientDataMapper;

    @Autowired
    public ClientDataManager(ClientDataMapper clientDataMapper) {
        this.clientDataMapper=clientDataMapper;
    }



    public ClientData createClientData(ClientData clientData, UserInfo userInfo) {
        clientData.setUserId(userInfo.getUserId());
        clientDataMapper.insert(clientData);
        return clientData;
    }

    public ClientData updateClientData(ClientData clientData, UserInfo userInfo) {
        clientData.setUserId(userInfo.getUserId());
        clientDataMapper.updateById(clientData);
        return  clientData;
    }

    public void deleteClientData(UserInfo userInfo) {
        var wrapper = new LambdaQueryWrapper<ClientData>();
        wrapper.eq(ClientData::getUserId, userInfo.getUserId());
        clientDataMapper.delete(wrapper);
    }

    public ClientData getClientData(UserInfo userInfo) {
        var wrapper = new LambdaQueryWrapper<ClientData>();
        wrapper.eq(ClientData::getUserId, userInfo.getUserId());
        var data = clientDataMapper.selectOne(wrapper);
        if(data == null) {
            log.warn("get client data failed, client data not exist, user id = {}", userInfo.getUserId());
            throw new NotExistException("get client data failed, client data not exist, user id = " + userInfo.getUserId());
        }
        return data;
    }
}
