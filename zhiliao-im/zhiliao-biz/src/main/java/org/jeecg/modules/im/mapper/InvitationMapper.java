package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Invitation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.im.entity.query_helper.QInvitation;

/**
 * <p>
 * 用户邀请记录 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2023-01-12
 */
@Mapper
public interface InvitationMapper extends BaseMapper<Invitation> {
    MyPage<Invitation> pagination(@Param("pg") MyPage<Invitation> pg, @Param("q") QInvitation q);

}
