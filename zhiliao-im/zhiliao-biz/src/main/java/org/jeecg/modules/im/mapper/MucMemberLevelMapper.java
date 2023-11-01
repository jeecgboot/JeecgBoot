package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.MucMemberLevel;
import org.jeecg.modules.im.entity.query_helper.QMucMemberLevel;

/**
 * <p>
 * 群组用户等级 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
@Mapper
public interface MucMemberLevelMapper extends BaseMapper<MucMemberLevel> {
    MyPage<MucMemberLevel> pagination(@Param("pg") MyPage<MucMemberLevel> myPage, @Param("q") QMucMemberLevel q);
}
