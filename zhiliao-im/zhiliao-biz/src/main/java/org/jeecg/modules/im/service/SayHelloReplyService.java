package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.SayHelloReply;
import org.jeecg.modules.im.entity.query_helper.QSayHelloReply;

import java.util.List;

/**
 * <p>
 * 加好友回话 服务类
 * </p>
 *
 * @author junko
 * @since 2021-03-03
 */
public interface SayHelloReplyService extends IService<SayHelloReply> {
    List<SayHelloReply> findByHelloId(Integer helloId,Boolean isSend);
    IPage<SayHelloReply> pagination(MyPage<SayHelloReply> page, QSayHelloReply q);

}
