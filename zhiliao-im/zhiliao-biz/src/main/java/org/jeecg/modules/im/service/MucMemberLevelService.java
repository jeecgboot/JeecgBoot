package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.MucMemberLevel;
import org.jeecg.modules.im.entity.query_helper.QMucMemberLevel;

/**
 * <p>
 * 群组用户等级 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
public interface MucMemberLevelService extends IService<MucMemberLevel> {
    IPage<MucMemberLevel> pagination(MyPage<MucMemberLevel> page, QMucMemberLevel q);

}
