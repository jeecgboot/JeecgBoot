package org.jeecg.modules.test.sharding.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.test.sharding.entity.ShardingSysLog;
import org.jeecg.modules.test.sharding.mapper.ShardingSysLogMapper;
import org.jeecg.modules.test.sharding.service.IShardingSysLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统日志表 服务实现类
 * </p>
 */
@Service
@DS("sharding")
public class ShardingSysLogServiceImpl extends ServiceImpl<ShardingSysLogMapper, ShardingSysLog> implements IShardingSysLogService {

}
