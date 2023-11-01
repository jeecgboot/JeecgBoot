package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Friend;
import org.jeecg.modules.im.entity.SayHello;
import org.jeecg.modules.im.entity.query_helper.QFriend;
import org.jeecg.modules.im.entity.query_helper.QSayHello;

import java.util.List;

/**
 * <p>
 * 加好友的回话记录 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-01-26
 */
@Mapper
public interface SayHelloMapper extends BaseMapper<SayHello> {
    MyPage<SayHello> pagination(@Param("pg") MyPage<SayHello> pg, @Param("q") QSayHello q);

    List<SayHello> findAllSend(Integer userId);
    List<SayHello> findAllReceive(Integer userId);
    SayHello findLatest(Integer userId,Integer toUserId);
    SayHello findById(Integer userId,Integer id);
}
