package com.bomaos.common.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bomaos.common.core.web.PageParam;
import com.bomaos.common.core.web.PageResult;
import com.bomaos.common.system.entity.DictionaryData;
import com.bomaos.common.system.mapper.DictionaryDataMapper;
import com.bomaos.common.system.service.DictionaryDataService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 字典项服务实现类
 * Created by Panyoujie on 2020-03-14 11:29:04
 */
@Service
public class DictionaryDataServiceImpl extends ServiceImpl<DictionaryDataMapper, DictionaryData> implements DictionaryDataService {

    @Override
    public PageResult<DictionaryData> listPage(PageParam<DictionaryData> page) {
        List<DictionaryData> records = baseMapper.listPage(page);
        return new PageResult<>(records, page.getTotal());
    }

    @Override
    public List<DictionaryData> listAll(Map<String, Object> page) {
        return baseMapper.listAll(page);
    }

    @Override
    public List<DictionaryData> listByDictCode(String dictCode) {
        PageParam<DictionaryData> pageParam = new PageParam<>();
        pageParam.put("dictCode", dictCode).setDefaultOrder(new String[]{"sort_number"}, null);
        List<DictionaryData> records = baseMapper.listAll(pageParam.getNoPageParam());
        return pageParam.sortRecords(records);
    }

    @Override
    public DictionaryData listByDictCodeAndName(String dictCode, String dictDataName) {
        PageParam<DictionaryData> pageParam = new PageParam<>();
        pageParam.put("dictCode", dictCode).put("dictDataName", dictDataName);
        List<DictionaryData> records = baseMapper.listAll(pageParam.getNoPageParam());
        return pageParam.getOne(records);
    }

}
