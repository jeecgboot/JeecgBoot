package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.MucMemberLevelCtg;
import org.jeecg.modules.im.entity.query_helper.QMucMemberLevelCtg;

/**
 * <p>
 * 群组用户等级分类 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
public interface MucMemberLevelCtgService extends IService<MucMemberLevelCtg> {
    IPage<MucMemberLevelCtg> pagination(MyPage<MucMemberLevelCtg> page, QMucMemberLevelCtg q);

    Result<Object> createOrUpdate(MucMemberLevelCtg ctg);
}
