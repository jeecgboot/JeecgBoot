package org.jeecg.modules.bjcl.message.service.impl;

import org.jeecg.modules.bjcl.message.entity.Message;
import org.jeecg.modules.bjcl.message.mapper.MessageMapper;
import org.jeecg.modules.bjcl.message.service.IMessageService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 留言
 * @Author: jeecg-boot
 * @Date:   2019-07-23
 * @Version: V1.0
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

}
