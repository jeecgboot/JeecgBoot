package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.entity.Provider;
import org.jeecg.modules.business.mapper.ProviderMapper;
import org.jeecg.modules.business.service.IProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: provider
 * @Author: jeecg-boot
 * @Date:   2024-02-14
 * @Version: V1.0
 */
@Service
public class ProviderServiceImpl extends ServiceImpl<ProviderMapper, Provider> implements IProviderService {
    @Autowired
    private ProviderMapper providerMapper;
    @Override
    public List<Provider> listByMabangIds(List<String> ids) {
        return providerMapper.listByMabangIds(ids);
    }
}
