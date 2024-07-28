package com.shop.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.common.core.web.PageParam;
import com.shop.common.core.web.PageResult;
import com.shop.entity.Pays;
import com.shop.mapper.PaysMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 支付配置服务实现类
 * 2021-03-29 11:06:11
 */
@Service
@Transactional
public class PaysService extends ServiceImpl<PaysMapper, Pays> {

    public PageResult<Pays> listPage(PageParam<Pays> page) {
        List<Pays> records = baseMapper.listPage(page);
        return new PageResult<>(records, page.getTotal());
    }

    public List<Pays> listAll(Map<String, Object> page) {
        return baseMapper.listAll(page);
    }

}
