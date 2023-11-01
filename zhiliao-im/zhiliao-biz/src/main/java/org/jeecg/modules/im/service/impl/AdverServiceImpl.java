package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Adver;
import org.jeecg.modules.im.entity.query_helper.QAdver;
import org.jeecg.modules.im.mapper.AdverMapper;
import org.jeecg.modules.im.service.AdverService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 广告 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-19
 */
@Service
public class AdverServiceImpl extends BaseServiceImpl<AdverMapper, Adver> implements AdverService {

    @Autowired
    private AdverMapper adverMapper;
    @Override
    public IPage<Adver> pagination(MyPage<Adver> page, QAdver q) {
        return adverMapper.pagination(page,q);
    }
}
