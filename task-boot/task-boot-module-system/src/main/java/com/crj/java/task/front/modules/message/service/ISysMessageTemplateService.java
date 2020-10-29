package com.crj.java.task.front.modules.message.service;

import java.util.List;

import com.crj.java.task.front.common.system.base.service.TaskService;
import com.crj.java.task.front.modules.message.entity.SysMessageTemplate;

/**
 * @Description: 消息模板
 * @Author: Crj
 * @Date:  2019-04-09
 * @Version: V1.0
 */
public interface ISysMessageTemplateService extends TaskService<SysMessageTemplate> {
    List<SysMessageTemplate> selectByCode(String code);
}
