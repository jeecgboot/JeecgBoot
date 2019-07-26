package org.jeecg.modules.bjcl.config.service.impl;

import org.jeecg.modules.bjcl.config.entity.Config;
import org.jeecg.modules.bjcl.config.mapper.ConfigMapper;
import org.jeecg.modules.bjcl.config.service.IConfigService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 配置
 * @Author: jeecg-boot
 * @Date:   2019-07-23
 * @Version: V1.0
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements IConfigService {

}
