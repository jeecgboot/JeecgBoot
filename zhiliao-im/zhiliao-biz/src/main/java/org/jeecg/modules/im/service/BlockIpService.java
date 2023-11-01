package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.BlockIp;
import org.jeecg.modules.im.entity.query_helper.QBlockIp;

/**
 * <p>
 * 前台ip黑名单 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-19
 */
public interface BlockIpService extends IService<BlockIp> {
    IPage<BlockIp> pagination(MyPage<BlockIp> page, QBlockIp q);
    Result<Object> checkIp(String ip);
    Result<Object> createOrUpdate(BlockIp blockIp);
    Result<Object> del(String ids);
}
