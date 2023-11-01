package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.MucMemberLevelCtg;
import org.jeecg.modules.im.entity.query_helper.QMucMemberLevelCtg;
import org.jeecg.modules.im.mapper.MucMemberLevelCtgMapper;
import org.jeecg.modules.im.service.MucMemberLevelCtgService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 群组用户等级分类 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
@Service
public class MucMemberLevelCtgServiceImpl extends BaseServiceImpl<MucMemberLevelCtgMapper, MucMemberLevelCtg> implements MucMemberLevelCtgService {
    @Autowired
    private MucMemberLevelCtgMapper mucMemberLevelCtgMapper;

    @Override
    public IPage<MucMemberLevelCtg> pagination(MyPage<MucMemberLevelCtg> page, QMucMemberLevelCtg q) {
        return mucMemberLevelCtgMapper.pagination(page,q);
    }

    @Override
    public Result<Object> createOrUpdate(MucMemberLevelCtg ctg) {
        if(ctg.getId()!=null){
            return success(updateById(ctg));
        }
        return success(save(ctg));
    }
}
