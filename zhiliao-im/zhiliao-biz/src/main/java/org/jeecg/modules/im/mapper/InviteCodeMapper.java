package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.InviteCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.im.entity.Link;
import org.jeecg.modules.im.entity.query_helper.QInviteCode;
import org.jeecg.modules.im.entity.query_helper.QLink;

/**
 * <p>
 * 用户邀请码 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2023-01-12
 */
@Mapper
public interface InviteCodeMapper extends BaseMapper<InviteCode> {
    InviteCode findByCode(String code);
    MyPage<InviteCode> pagination(@Param("pg") MyPage<InviteCode> pg, @Param("q") QInviteCode q);

}
