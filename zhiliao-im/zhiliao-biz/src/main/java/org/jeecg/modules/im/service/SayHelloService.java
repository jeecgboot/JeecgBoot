package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.SayHello;
import org.jeecg.modules.im.entity.SayHello;
import org.jeecg.modules.im.entity.query_helper.QSayHello;

import java.util.List;

/**
 * <p>
 * 加好友的回话记录 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-26
 */
public interface SayHelloService extends IService<SayHello> {
    IPage<SayHello> pagination(MyPage<SayHello> page, QSayHello q);

    Result<Object> sendAdd(Integer userId, Integer toUserId, Integer resource, String who, String remark);
    Result<Object> sendFollow(Integer userId, Integer toUserId, Integer resource);
    List<SayHello> findAllSend(Integer userId);
    List<SayHello> findAllReceive(Integer userId);
    SayHello findLatest(Integer userId,Integer toUserId);
    SayHello findById(Integer userId,Integer id);

    Result<Object> deal(Integer id, boolean isAccept,Integer userId);
    //回复
    Result<Object> reply(Integer id, String msg,Integer userId);
}
