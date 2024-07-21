package com.bomaos.website.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bomaos.common.core.web.PageParam;
import com.bomaos.website.entity.Website;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 网站设置Mapper接口
 * Created by Panyoujie on 2021-06-06 02:14:54
 */
public interface WebsiteMapper extends BaseMapper<Website> {

    /**
     * 分页查询
     */
    List<Website> listPage(@Param("page") PageParam<Website> page);

    /**
     * 查询全部
     */
    List<Website> listAll(@Param("page") Map<String, Object> page);

}
