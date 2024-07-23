package com.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.common.core.web.PageParam;
import com.shop.entity.ShopSettings;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商店设置Mapper接口
 * Created by Panyoujie on 2021-07-04 03:54:31
 */
public interface ShopSettingsMapper extends BaseMapper<ShopSettings> {

    /**
     * 分页查询
     */
    List<ShopSettings> listPage(@Param("page") PageParam<ShopSettings> page);

    /**
     * 查询全部
     */
    List<ShopSettings> listAll(@Param("page") Map<String, Object> page);

}
