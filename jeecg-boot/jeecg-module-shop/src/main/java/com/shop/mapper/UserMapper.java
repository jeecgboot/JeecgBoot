package com.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.common.core.web.PageParam;
import com.shop.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户Mapper接口
 * Created by Panyoujie on 2018-12-24 16:10
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 分页查询
     */
    List<User> listPage(@Param("page") PageParam<User> page);

    /**
     * 查询全部
     */
    List<User> listAll(@Param("page") Map<String, Object> page);

}
