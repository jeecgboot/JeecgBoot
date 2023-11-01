package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.MucMsg;
import org.jeecg.modules.im.entity.query_helper.QMucMsg;
import org.jeecg.modules.im.mapper.MucMsgMapper;
import org.jeecg.modules.im.service.MucMsgService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 群聊消息 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-26
 */
@Service
public class MucMsgServiceImpl extends BaseServiceImpl<MucMsgMapper, MucMsg> implements MucMsgService {

    @Autowired
    private MucMsgMapper mucMsgMapper;
    @Override
    public IPage<MucMsg> pagination(MyPage<MucMsg> page, QMucMsg q) {
        return mucMsgMapper.pagination(page,q);
    }

    @Override
    public List<MucMsg> pageApi(QMucMsg q) {
        return mucMsgMapper.pageApi(q);
    }

    @Override
    public Result<Object> clearAll(Integer mucId) {
        return null;
    }

    @Override
    public Result<Object> delByUserOfMuc(Integer userId, Integer mucId) {
        return null;
    }

    @Override
    public Result<Object> clearByUser(Integer userId) {
        return null;
    }

    @Override
    public Result<Object> delByIds(Integer mucId, String msgIds) {
        return null;
    }
}
