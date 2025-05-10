package org.jeecg.modules.openapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.openapi.entity.OpenApiLog;
import org.jeecg.modules.openapi.mapper.OpenApiLogMapper;
import org.jeecg.modules.openapi.service.OpenApiLogService;
import org.springframework.stereotype.Service;

/**
 * @date 2024/12/10 9:53
 */
@Service
public class OpenApiLogServiceImpl extends ServiceImpl<OpenApiLogMapper, OpenApiLog> implements OpenApiLogService {
}
