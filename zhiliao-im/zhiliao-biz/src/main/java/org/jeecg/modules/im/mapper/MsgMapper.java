package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Gif;
import org.jeecg.modules.im.entity.Msg;
import org.jeecg.modules.im.entity.query_helper.QGif;
import org.jeecg.modules.im.entity.query_helper.QMsg;

import java.util.List;

/**
 * <p>
 * 聊天消息 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-01-26
 */
@Mapper
public interface MsgMapper extends BaseMapper<Msg> {
    MyPage<Msg> pagination(@Param("pg") MyPage<Msg> pg, @Param("q") QMsg q);

    List<Msg> paginationApi(@Param("q") QMsg q);

    Long deleteLogic(Integer userId, Integer toUserId,Long tsDelete);
    Long deleteLogicBoth(Integer userId, Integer toUserId,Long tsDelete);

    Long deleteLogicWithIds(String ids,Integer type,Long tsDelete);

    Long readAllReceive(Integer userId, Integer toUserId,Long tsRead);

    List<Msg> findByStanzaId(String stanzaId);
    Msg findByStanzaIdOfSend(String stanzaId,boolean isSend);
}
