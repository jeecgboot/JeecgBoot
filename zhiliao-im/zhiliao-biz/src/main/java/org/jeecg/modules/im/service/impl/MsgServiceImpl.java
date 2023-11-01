package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Update;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Msg;
import org.jeecg.modules.im.entity.query_helper.QMsg;
import org.jeecg.modules.im.mapper.MsgMapper;
import org.jeecg.modules.im.service.MsgService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 聊天消息 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-26
 */
@Service
public class MsgServiceImpl extends BaseServiceImpl<MsgMapper, Msg> implements MsgService {
    @Autowired
    private MsgMapper msgMapper;
    @Override
    public IPage<Msg> pagination(MyPage<Msg> page, QMsg q) {
        return msgMapper.pagination(page,q);
    }

    @Override
    public List<Msg> paginationApi(QMsg q) {
        return msgMapper.paginationApi(q);
    }

    @Override
    public Result<Object> deleteLogic(Integer fromUserId,Integer toUserId) {
        return success(msgMapper.deleteLogic(fromUserId,toUserId,getTs()));
    }
    @Override
    public Result<Object> deleteLogicBoth(Integer fromUserId,Integer toUserId) {
        return success(msgMapper.deleteLogicBoth(fromUserId,toUserId,getTs()));
    }


    @Override
    public Result<Object> del(String ids,Integer type) {
        if(isEmpty(ids)){
            return fail();
        }
        return success(msgMapper.deleteLogicWithIds("'"+ids+"'",type,getTs()));
    }

    @Override
    public Result<Object> readAllReceive(int userId, int toUserId) {
        msgMapper.readAllReceive(userId,toUserId,getTs());
        return null;
    }

    @Override
    public List<Msg> findByStanzaId(String stanzaId) {
        return msgMapper.findByStanzaId(stanzaId);
    }
    @Override
    public Msg findByStanzaIdOfSend(String stanzaId,boolean isSend) {
        return msgMapper.findByStanzaIdOfSend(stanzaId,isSend);
    }


}
