package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Channel;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.query_helper.QChannel;

/**
 * <p>
 * 频道 服务类
 * </p>
 *
 * @author junko
 * @since 2022-04-28
 */
public interface ChannelService extends IService<Channel> {
    IPage<Channel> pagination(MyPage<Channel> page, QChannel q);
    Result<Object> createOrUpdate(Channel channel);
    Channel findByName(String name);
}
