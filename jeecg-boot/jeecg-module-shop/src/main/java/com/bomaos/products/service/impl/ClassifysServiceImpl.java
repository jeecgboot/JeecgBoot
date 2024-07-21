package com.bomaos.products.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bomaos.common.core.web.PageParam;
import com.bomaos.common.core.web.PageResult;
import com.bomaos.products.entity.Classifys;
import com.bomaos.products.entity.Products;
import com.bomaos.products.mapper.ClassifysMapper;
import com.bomaos.products.mapper.ProductsMapper;
import com.bomaos.products.service.ClassifysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 分类服务实现类
 * Created by Panyoujie on 2021-03-27 20:22:00
 */
@Service
@Transactional
public class ClassifysServiceImpl extends ServiceImpl<ClassifysMapper, Classifys> implements ClassifysService {

    @Autowired
    private ProductsMapper productsMapper;

    @Override
    public PageResult<Classifys> listPage(PageParam<Classifys> page) {
        List<Classifys> records = baseMapper.listPage(page);
        return new PageResult<>(records, page.getTotal());
    }

    @Override
    public List<Classifys> listAll(Map<String, Object> page) {
        return baseMapper.listAll(page);
    }

    @Override
    public boolean removeByIds(Collection<?> idList) {
        for (Object serializable : idList) {
            long count = productsMapper.selectCount(new QueryWrapper<Products>().eq("classify_id", serializable));
            if (count > 0) {
                return false;
            } else {
                baseMapper.deleteById((Serializable) serializable);
            }
        }
        return true;
    }
}
