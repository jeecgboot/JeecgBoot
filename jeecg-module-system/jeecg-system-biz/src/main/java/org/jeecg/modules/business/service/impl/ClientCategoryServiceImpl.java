package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.entity.ClientCategory;
import org.jeecg.modules.business.mapper.ClientCategoryMapper;
import org.jeecg.modules.business.service.IClientCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: client category
 * @Author: jeecg-boot
 * @Date:   2023-10-19
 * @Version: V1.0
 */
@Service
public class ClientCategoryServiceImpl extends ServiceImpl<ClientCategoryMapper, ClientCategory> implements IClientCategoryService {
    @Autowired
    private ClientCategoryMapper clientCategoryMapper;
    @Override
    public String getClientCategoryByClientId(String clientId) {
        return clientCategoryMapper.getClientCategoryByClientId(clientId);
    }

    @Override
    public String getIdByCode(String categoryName) {
        return clientCategoryMapper.getIdByCode(categoryName);
    }
}
