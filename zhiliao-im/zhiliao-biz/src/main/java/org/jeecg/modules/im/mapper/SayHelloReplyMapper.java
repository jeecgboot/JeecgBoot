package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.SayHelloReply;
import org.jeecg.modules.im.entity.query_helper.QSayHelloReply;

import java.util.List;

/**
 * <p>
 * 加好友回话 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-03-03
 */
@Mapper
public interface SayHelloReplyMapper extends BaseMapper<SayHelloReply> {
    List<SayHelloReply> findByHelloId(Integer helloId,Boolean isSend);
    MyPage<SayHelloReply> pagination(@Param("pg") MyPage<SayHelloReply> pg, @Param("q") QSayHelloReply q);

}
