package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Adver;
import org.jeecg.modules.im.entity.query_helper.QAdver;

/**
 * <p>
 * 广告 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-19
 */
public interface AdverService extends IService<Adver> {
    IPage<Adver> pagination(MyPage<Adver> page, QAdver q);
}
