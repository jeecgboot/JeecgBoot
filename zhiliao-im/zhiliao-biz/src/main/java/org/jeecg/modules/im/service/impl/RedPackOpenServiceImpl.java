package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.RedPackOpen;
import org.jeecg.modules.im.entity.query_helper.QRedPackOpen;
import org.jeecg.modules.im.mapper.RedPackOpenMapper;
import org.jeecg.modules.im.service.RedPackOpenService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 拆红包记录 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
@Service
public class RedPackOpenServiceImpl extends BaseServiceImpl<RedPackOpenMapper, RedPackOpen> implements RedPackOpenService {
    @Autowired
    private RedPackOpenMapper redPackOpenMapper;
    @Override
    public IPage<RedPackOpen> pagination(MyPage<RedPackOpen> page, QRedPackOpen q) {
        return redPackOpenMapper.pagination(page,q);
    }
}
