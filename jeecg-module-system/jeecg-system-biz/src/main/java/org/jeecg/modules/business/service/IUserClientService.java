package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.entity.Client;
import org.jeecg.modules.business.entity.UserClient;

public interface IUserClientService extends IService<UserClient> {
    Client getClientByUserId(String userId);
}
