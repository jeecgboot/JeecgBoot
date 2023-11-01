package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.MucMsg;
import org.jeecg.modules.im.entity.query_helper.QMucMsg;

import java.util.List;

/**
 * <p>
 * 群聊消息 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-01-26
 */
@Mapper
public interface MucMsgMapper extends BaseMapper<MucMsg> {
    MyPage<MucMsg> pagination(@Param("pg") MyPage<MucMsg> pg, @Param("q") QMucMsg q);
    List<MucMsg> pageApi(@Param("q") QMucMsg q);
}
