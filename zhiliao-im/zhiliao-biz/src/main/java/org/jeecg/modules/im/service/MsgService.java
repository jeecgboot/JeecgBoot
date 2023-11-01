package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.util.Kv;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Gif;
import org.jeecg.modules.im.entity.Msg;
import org.jeecg.modules.im.entity.query_helper.QGif;
import org.jeecg.modules.im.entity.query_helper.QMsg;

import java.util.List;

/**
 * <p>
 * 聊天消息 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-26
 */
public interface MsgService extends IService<Msg> {
    IPage<Msg> pagination(MyPage<Msg> page, QMsg q);
    //分页查询，
    List<Msg> paginationApi(QMsg q);

    Result<Object> deleteLogic(Integer fromUserId,Integer toUserId);
    Result<Object> deleteLogicBoth(Integer fromUserId,Integer toUserId);

    Result<Object> del(String ids,Integer type);

    Result<Object> readAllReceive(int userId,int toUserId);

    List<Msg> findByStanzaId(String stanzaId);

    Msg findByStanzaIdOfSend(String stanzaId,boolean isSend);
}
