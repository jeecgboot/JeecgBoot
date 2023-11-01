package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.exception.BusinessException;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Channel;
import org.jeecg.modules.im.entity.ClientConfig;
import org.jeecg.modules.im.entity.query_helper.QChannel;
import org.jeecg.modules.im.mapper.ChannelMapper;
import org.jeecg.modules.im.service.ChannelService;
import org.jeecg.modules.im.service.ClientConfigService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;

/**
 * <p>
 * 频道 服务实现类
 * </p>
 *
 * @author junko
 * @since 2022-04-28
 */
@Service
@Slf4j
public class ChannelServiceImpl extends BaseServiceImpl<ChannelMapper, Channel> implements ChannelService {
    @Autowired
    private ChannelMapper channelMapper;
    @Resource
    private ClientConfigService clientConfigService;
    @Override
    public IPage<Channel> pagination(MyPage<Channel> page, QChannel q) {
        return channelMapper.pagination(page, q);
    }

    @Override
    public Result<Object> createOrUpdate(Channel channel) {
        if(channel.getId()==null){
            return consoleCreate(channel);
        }
        return consoleUpdate(channel);
    }

    @Override
    public Channel findByName(String name) {
        LambdaQueryWrapper<Channel> q = new LambdaQueryWrapper<>();
        return getOne(q.eq(Channel::getName,name));
    }

    private Result<Object> consoleCreate(Channel channel) {
        ClientConfig config = clientConfigService.get();
        if(config.getChannelNameUnique()&&findByName(channel.getName())!=null){
            return fail("群组名称已存在");
        }
        try {
            channel.setTsCreate(getTs());
            if(!save(channel)){
                return fail("创建频道失败 ");
            }
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return fail();
        } catch (Exception e) {
            log.error("后台创建频道异常：channel={},e={}", channel, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (e instanceof BusinessException) {
                return fail(e.getMessage());
            }
            return fail();
        }
    }
    private Result<Object> consoleUpdate(Channel channel) {
        return updateById(channel)?success():fail();
    }

}
