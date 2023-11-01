package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.MucMemberLevel;
import org.jeecg.modules.im.entity.query_helper.QMucMemberLevel;
import org.jeecg.modules.im.mapper.MucMemberLevelMapper;
import org.jeecg.modules.im.service.MucMemberLevelService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 群组用户等级 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
@Service
public class MucMemberLevelServiceImpl extends BaseServiceImpl<MucMemberLevelMapper, MucMemberLevel> implements MucMemberLevelService {
    @Autowired
    private MucMemberLevelMapper mucMemberLevelMapper;

    @Override
    public IPage<MucMemberLevel> pagination(MyPage<MucMemberLevel> page, QMucMemberLevel q) {
        return mucMemberLevelMapper.pagination(page,q);
    }
}
