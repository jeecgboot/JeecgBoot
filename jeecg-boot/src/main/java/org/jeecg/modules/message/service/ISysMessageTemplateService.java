package org.jeecg.modules.message.service;

import org.jeecg.modules.message.entity.SysMessageTemplate;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 消息模板
 * @author： jeecg-boot
 * @date：   2019-04-09
 * @version： V1.0
 */
public interface ISysMessageTemplateService extends IService<SysMessageTemplate> {
    List<SysMessageTemplate> selectByCode(String code);
}
