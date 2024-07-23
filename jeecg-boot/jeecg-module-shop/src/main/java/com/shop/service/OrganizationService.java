package com.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.common.core.web.PageParam;
import com.shop.common.core.web.PageResult;
import com.shop.entity.Organization;

import java.util.List;
import java.util.Map;

/**
 * 组织机构服务类
 * Created by Panyoujie on 2020-03-14 11:29:04
 */
public interface OrganizationService extends IService<Organization> {

    /**
     * 关联分页查询
     */
    PageResult<Organization> listPage(PageParam<Organization> page);

    /**
     * 关联查询所有
     */
    List<Organization> listAll(Map<String, Object> page);

}
