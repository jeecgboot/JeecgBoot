package com.crj.java.task.front.modules.message.service.impl;

import com.crj.java.task.front.common.system.base.service.impl.TaskServiceImpl;
import com.crj.java.task.front.modules.message.entity.SysMessageTemplate;
import com.crj.java.task.front.modules.message.mapper.SysMessageTemplateMapper;
import com.crj.java.task.front.modules.message.service.ISysMessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @Description: 消息模板
 * @Author: Crj
 * @Date:  2019-04-09
 * @Version: V1.0
 */
@Service
public class SysMessageTemplateServiceImpl extends TaskServiceImpl<SysMessageTemplateMapper, SysMessageTemplate> implements ISysMessageTemplateService {

    @Autowired
    private SysMessageTemplateMapper sysMessageTemplateMapper;


    @Override
    public List<SysMessageTemplate> selectByCode(String code) {
        return sysMessageTemplateMapper.selectByCode(code);
    }
}
