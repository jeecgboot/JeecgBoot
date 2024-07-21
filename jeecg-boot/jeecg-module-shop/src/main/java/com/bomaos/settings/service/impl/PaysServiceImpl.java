package com.bomaos.settings.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bomaos.common.core.web.PageParam;
import com.bomaos.common.core.web.PageResult;
import com.bomaos.settings.entity.Pays;
import com.bomaos.settings.mapper.PaysMapper;
import com.bomaos.settings.service.PaysService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 支付配置服务实现类
 * Created by Panyoujie on 2021-03-29 11:06:11
 */
@Service
@Transactional
public class PaysServiceImpl extends ServiceImpl<PaysMapper, Pays> implements PaysService {

    @Override
    public PageResult<Pays> listPage(PageParam<Pays> page) {
        List<Pays> records = baseMapper.listPage(page);
        return new PageResult<>(records, page.getTotal());
    }

    @Override
    public List<Pays> listAll(Map<String, Object> page) {
        return baseMapper.listAll(page);
    }

}
