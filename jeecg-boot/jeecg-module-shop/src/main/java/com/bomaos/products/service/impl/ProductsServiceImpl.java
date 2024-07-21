package com.bomaos.products.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bomaos.carmi.entity.Cards;
import com.bomaos.carmi.mapper.CardsMapper;
import com.bomaos.common.core.web.PageParam;
import com.bomaos.common.core.web.PageResult;
import com.bomaos.products.entity.Products;
import com.bomaos.products.mapper.ProductsMapper;
import com.bomaos.products.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 商品服务实现类
 * Created by Panyoujie on 2021-03-27 20:22:00
 */
@Service
@Transactional
public class ProductsServiceImpl extends ServiceImpl<ProductsMapper, Products> implements ProductsService {

    @Autowired
    private CardsMapper cardsMapper;

    @Override
    public PageResult<Products> listPage(PageParam<Products> page) {
        List<Products> records = baseMapper.listPage(page);
        return new PageResult<>(records, page.getTotal());
    }

    @Override
    public List<Products> listAll(Map<String, Object> page) {
        return baseMapper.listAll(page);
    }

    @Override
    public List<Products> getRandomProductList(int limit) {
        return baseMapper.getRandomProductList(limit);
    }

    @Override
    public boolean removeByIds(Collection<?> idList) {
        for (Object serializable : idList) {
            long count = cardsMapper.selectCount(new QueryWrapper<Cards>().eq("product_id", serializable));
            if (count > 0) {
                return false;
            } else {
                baseMapper.deleteById((Serializable) serializable);
            }
        }
        return true;
    }
}
