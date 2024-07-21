package com.bomaos.settings.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bomaos.common.core.web.PageParam;
import com.bomaos.settings.entity.Pays;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 支付配置Mapper接口
 * Created by Panyoujie on 2021-03-29 11:06:11
 */
public interface PaysMapper extends BaseMapper<Pays> {

    /**
     * 分页查询
     */
    List<Pays> listPage(@Param("page") PageParam<Pays> page);

    /**
     * 查询全部
     */
    List<Pays> listAll(@Param("page") Map<String, Object> page);

}
