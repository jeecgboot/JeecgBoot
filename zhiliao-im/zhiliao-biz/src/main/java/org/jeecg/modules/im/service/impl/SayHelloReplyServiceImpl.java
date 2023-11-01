package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.SayHelloReply;
import org.jeecg.modules.im.entity.query_helper.QSayHelloReply;
import org.jeecg.modules.im.mapper.SayHelloReplyMapper;
import org.jeecg.modules.im.service.SayHelloReplyService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 加好友回话 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-03-03
 */
@Service
public class SayHelloReplyServiceImpl extends BaseServiceImpl<SayHelloReplyMapper, SayHelloReply> implements SayHelloReplyService {

    @Autowired
    private SayHelloReplyMapper sayHelloReplyMapper;
    @Override
    public List<SayHelloReply> findByHelloId(Integer helloId,Boolean isSend) {
        return sayHelloReplyMapper.findByHelloId(helloId,isSend);
    }

    @Override
    public IPage<SayHelloReply> pagination(MyPage<SayHelloReply> page, QSayHelloReply q) {
        return sayHelloReplyMapper.pagination(page,q);
    }
}
