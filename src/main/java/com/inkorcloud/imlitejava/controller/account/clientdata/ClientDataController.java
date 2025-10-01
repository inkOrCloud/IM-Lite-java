package com.inkorcloud.imlitejava.controller.account.clientdata;

import com.inkorcloud.imlitejava.controller.account.auth.UserInfoProvider;
import com.inkorcloud.imlitejava.service.data.account.clientdata.ClientDataManager;
import com.inkorcloud.imlitejava.util.jwt.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client_data")
public class ClientDataController {
    private final ClientDataManager clientDataManager;

    @Autowired
    public ClientDataController(ClientDataManager clientDataManager) {
        this.clientDataManager = clientDataManager;
    }

    @RequestMapping("get")
    public String getClientData(@UserInfoProvider UserInfo userInfo) {
        return clientDataManager.getClientData(userInfo).getClientData();
    }

    @RequestMapping("update")
    public String updateClientData(String clientData, @UserInfoProvider UserInfo userInfo) {
        var data = clientDataManager.getClientData(userInfo);
        data.setClientData(clientData);
        clientDataManager.updateClientData(data, userInfo);
        return data.getClientData();
    }
}
