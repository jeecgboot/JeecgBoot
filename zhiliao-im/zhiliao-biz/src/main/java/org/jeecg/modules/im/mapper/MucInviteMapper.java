package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.MucInvite;
import org.jeecg.modules.im.entity.MucInvite;
import org.jeecg.modules.im.entity.query_helper.QMucInvite;

/**
 * <p>
 * 加群邀请 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-03-06
 */
@Mapper
public interface MucInviteMapper extends BaseMapper<MucInvite> {
    MyPage<MucInvite> pagination(@Param("pg") MyPage<MucInvite> pg, @Param("q") QMucInvite q);
    

    MucInvite findLatestUnDeal(Integer inviter, Integer mucId, Integer invitee);

    int invalidOfUserByMuc(Integer invitee, Integer mucId);
}
