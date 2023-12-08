package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.business.entity.Client;
import org.jeecg.modules.business.entity.UserClient;
import org.jeecg.modules.business.mapper.ClientUserMapper;
import org.jeecg.modules.business.service.IUserClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserClientServiceImpl  extends ServiceImpl<ClientUserMapper, UserClient> implements IUserClientService {
    @Autowired
    private ClientUserMapper clientUserMapper;
    @Override
    public Client getClientByUserId(String userId) {
        return clientUserMapper.selectClientByUserId(userId);
    }

    @Override
    public List<Client> listClients() {
        return clientUserMapper.listClients();
    }

    @Override
    public Client getClientMinInfoByUserId(String userId) {
        return clientUserMapper.getClientMinInfoByUserId(userId);
    }
}
