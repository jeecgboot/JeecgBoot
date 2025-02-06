package org.jeecg.modules.business.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.entity.SensitiveAttribute;
import org.jeecg.modules.business.mapper.SensitiveAttributeMapper;
import org.jeecg.modules.business.service.ISensitiveAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: sensitive_attribute
 * @Author: jeecg-boot
 * @Date:   2023-10-03
 * @Version: V1.0
 */
@Slf4j
@Service
public class SensitiveAttributeServiceImpl extends ServiceImpl<SensitiveAttributeMapper, SensitiveAttribute> implements ISensitiveAttributeService {
    @Autowired
    private SensitiveAttributeMapper sensitiveAttributeMapper;
    @Override
    public SensitiveAttribute getHighestPriorityAttribute(String orderId) {
        return sensitiveAttributeMapper.getHighestPriorityAttribute(orderId);
    }
    @Override
    public String getHighestPriorityAttributeId(String orderId) {
        return sensitiveAttributeMapper.getHighestPriorityAttributeId(orderId);
    }

    @Override
    public List<SensitiveAttribute> listIdAndPriority() {
        return sensitiveAttributeMapper.listIdAndPriority();
    }

    @Override
    public String getNameByAttributes(SensitiveAttribute sensitiveAttribute) {
        return sensitiveAttributeMapper.getNameByAttributes(sensitiveAttribute);
    }

    @Override
    public SensitiveAttribute getByZhName(String zhName) {
        return sensitiveAttributeMapper.getByZhName(zhName);
    }
}
