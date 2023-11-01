package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.Helps;
import org.jeecg.modules.im.mapper.HelpsMapper;
import org.jeecg.modules.im.service.HelpsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 帮助 服务实现类
 * </p>
 *
 * @author junko
 * @since 2023-02-23
 */
@Service
public class HelpsServiceImpl extends BaseServiceImpl<HelpsMapper, Helps> implements HelpsService {

    @Override
    public Result<Object> findAll() {
        LambdaQueryWrapper<Helps> wrapper = new LambdaQueryWrapper();
        return success(list(wrapper));
    }
}
