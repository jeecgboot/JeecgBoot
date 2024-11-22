package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.entity.ExtraFeeOption;
import org.jeecg.modules.business.mapper.ExtraFeeOptionMapper;
import org.jeecg.modules.business.service.IExtraFeeOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: extra fees options
 * @Author: jeecg-boot
 * @Date:   2024-11-15
 * @Version: V1.0
 */
@Service
public class ExtraFeeOptionServiceImpl extends ServiceImpl<ExtraFeeOptionMapper, ExtraFeeOption> implements IExtraFeeOptionService {
    @Autowired
    private ExtraFeeOptionMapper optionMapper;

    @Override
    public ExtraFeeOption getByName(String optionName) {
        return optionMapper.selectByEnName(optionName);
    }
}
