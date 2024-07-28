package com.shop.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.common.core.web.PageParam;
import com.shop.common.core.web.PageResult;
import com.shop.entity.Organization;
import com.shop.mapper.OrganizationMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 组织机构服务实现类
 * 2020-03-14 11:29:04
 */
@Service
public class OrganizationService extends ServiceImpl<OrganizationMapper, Organization> {

    public PageResult<Organization> listPage(PageParam<Organization> page) {
        List<Organization> records = baseMapper.listPage(page);
        return new PageResult<>(records, page.getTotal());
    }

    public List<Organization> listAll(Map<String, Object> page) {
        return baseMapper.listAll(page);
    }

}
