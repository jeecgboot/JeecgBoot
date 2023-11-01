package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.MucMemberLevelConfig;
import org.jeecg.modules.im.entity.query_helper.QMucMemberLevelConfig;
import org.jeecg.modules.im.mapper.MucMemberLevelConfigMapper;
import org.jeecg.modules.im.service.MucMemberLevelConfigService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 群组用户等级配置 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
@Service
public class MucMemberLevelConfigServiceImpl extends BaseServiceImpl<MucMemberLevelConfigMapper, MucMemberLevelConfig> implements MucMemberLevelConfigService {
    @Autowired
    private MucMemberLevelConfigMapper mucMemberLevelConfigMapper;

    @Override
    public IPage<MucMemberLevelConfig> pagination(MyPage<MucMemberLevelConfig> page, QMucMemberLevelConfig q) {
        return mucMemberLevelConfigMapper.pagination(page,q);
    }
}
