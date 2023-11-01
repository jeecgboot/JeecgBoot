package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.ClientVer;
import org.jeecg.modules.im.entity.query_helper.QClientVer;
import org.jeecg.modules.im.mapper.ClientVerMapper;
import org.jeecg.modules.im.service.ClientVerService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * app版本 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-30
 */
@Service
public class ClientVerServiceImpl extends BaseServiceImpl<ClientVerMapper, ClientVer> implements ClientVerService {

    @Autowired
    private ClientVerMapper clientVerMapper;

    @Override
    public IPage<ClientVer> pagination(MyPage<ClientVer> page, QClientVer q) {
        return clientVerMapper.pagination(page,q);
    }

    @Override
    public ClientVer findLatestByPlatform(String platform) {
        return clientVerMapper.findLatestByPlatform(platform);
    }

    @Override
    public List<ClientVer> findLatestOfAll() {
        return clientVerMapper.findLatestOfAll();
    }
}
