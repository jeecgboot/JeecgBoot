package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.RedPackOpen;
import org.jeecg.modules.im.entity.query_helper.QRedPackOpen;

/**
 * <p>
 * 拆红包记录 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
public interface RedPackOpenService extends IService<RedPackOpen> {
    IPage<RedPackOpen> pagination(MyPage<RedPackOpen> page, QRedPackOpen q);
}
