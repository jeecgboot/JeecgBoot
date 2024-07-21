package com.bomaos.carmi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bomaos.carmi.entity.Cards;
import com.bomaos.carmi.vo.CardsDts;
import com.bomaos.common.core.web.JsonResult;
import com.bomaos.common.core.web.PageParam;
import com.bomaos.common.core.web.PageResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 卡密服务类
 * Created by Panyoujie on 2021-03-28 00:33:15
 */
public interface CardsService extends IService<Cards> {

    /**
     * 分页查询
     */
    PageResult<Cards> listPage(PageParam<Cards> page);

    /**
     * 查询所有
     */
    List<Cards> listAll(Map<String, Object> page);

    JsonResult addCards(CardsDts cardsDts);

    List<Cards> getCard(Integer status, Integer productId, Integer number);

    void export(HttpServletRequest request);
}
