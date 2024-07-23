package com.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.entity.Cards;
import com.shop.common.core.web.PageParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 卡密Mapper接口
 * Created by Panyoujie on 2021-03-28 00:33:15
 */
public interface CardsMapper extends BaseMapper<Cards> {

    /**
     * 分页查询
     */
    List<Cards> listPage(@Param("page") PageParam<Cards> page);

    /**
     * 查询全部
     */
    List<Cards> listAll(@Param("page") Map<String, Object> page);


    /**
     * 查询卡密
     *
     * @param status
     * @param productId
     * @return
     */
    List<Cards> getCard(Integer status, Integer productId, Integer number);

}
