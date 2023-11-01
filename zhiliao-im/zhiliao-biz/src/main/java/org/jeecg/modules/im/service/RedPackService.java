package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.RedPack;
import org.jeecg.modules.im.entity.query_helper.QRedPack;

/**
 * <p>
 * 红包 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
public interface RedPackService extends IService<RedPack> {
    IPage<RedPack> pagination(MyPage<RedPack> page, QRedPack q);
    //单聊发送
    Result<Object> send(RedPack redPack);
    //群聊发送
    Result<Object> sendMuc(RedPack redPack);
}
