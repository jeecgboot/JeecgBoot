package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.ClientVer;
import org.jeecg.modules.im.entity.query_helper.QClientVer;

import java.util.List;

/**
 * <p>
 * app版本 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-30
 */
public interface ClientVerService extends IService<ClientVer> {
    IPage<ClientVer> pagination(MyPage<ClientVer> page, QClientVer q);
    ClientVer findLatestByPlatform(String platform);
    List<ClientVer> findLatestOfAll();
}
