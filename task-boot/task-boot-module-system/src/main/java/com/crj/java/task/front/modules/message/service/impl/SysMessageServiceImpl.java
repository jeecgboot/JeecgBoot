package com.crj.java.task.front.modules.message.service.impl;

import com.crj.java.task.front.common.system.base.service.impl.TaskServiceImpl;
import com.crj.java.task.front.modules.message.entity.SysMessage;
import com.crj.java.task.front.modules.message.mapper.SysMessageMapper;
import com.crj.java.task.front.modules.message.service.ISysMessageService;
import org.springframework.stereotype.Service;

/**
 * @Description: 消息
 * @Author: Crj
 * @Date:  2019-04-09
 * @Version: V1.0
 */
@Service
public class SysMessageServiceImpl extends TaskServiceImpl<SysMessageMapper, SysMessage> implements ISysMessageService {

}
