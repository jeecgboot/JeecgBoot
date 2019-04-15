package org.jeecg.modules.system.service.impl;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.mapper.SysDictItemMapper;
import org.jeecg.modules.system.mapper.SysDictMapper;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.jeecg.modules.system.service.ISysDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author zhangweijian
 * @since 2018-12-28
 */
@Service()
@Slf4j
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

    @Autowired
    private SysDictMapper sysDictMapper;
    @Autowired
    private SysDictItemMapper sysDictItemMapper;
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private ISysDictItemService sysDictItemService;

	/**
	 * 通过查询指定code 获取字典
	 * @param code
	 * @return
	 */
	@Override
	@Cacheable(value = "dictCache",key = "#code")
	public List<Map<String, Object>> queryDictItemsByCode(String code) {
		log.info("无缓存dictList的时候调用这里！");
		return sysDictMapper.queryDictItemsByCode(code);
	}

	/**
	 * 通过查询指定code 获取字典值text
	 * @param code
	 * @param key
	 * @return
	 */

	@Override
	@Cacheable(value = "dictCache")
	public String queryDictTextByKey(String code, String key) {
		log.info("无缓存dictText的时候调用这里！");
		return sysDictMapper.queryDictTextByKey(code, key);
	}

	/**
	 * 通过查询指定table的 text code 获取字典
	 * dictTableCache采用redis缓存有效期10分钟
	 * @param table
	 * @param text
	 * @param code
	 * @return
	 */
	@Override
	@Cacheable(value = "dictTableCache")
	public List<Map<String, Object>> queryTableDictItemsByCode(String table, String text, String code) {
		log.info("无缓存dictTableList的时候调用这里！");
		return sysDictMapper.queryTableDictItemsByCode(table,text,code);
	}

	/**
	 * 通过查询指定table的 text code 获取字典值text
	 * dictTableCache采用redis缓存有效期10分钟
	 * @param table
	 * @param text
	 * @param code
	 * @param key
	 * @return
	 */
	@Override
	@Cacheable(value = "dictTableCache")
	public String queryTableDictTextByKey(String table,String text,String code, String key) {
		log.info("无缓存dictTable的时候调用这里！");
		return sysDictMapper.queryTableDictTextByKey(table,text,code,key);
	}

    /**
     * 根据字典类型id删除关联表中其对应的数据
     */
    @Override
    public boolean deleteByDictId(SysDict sysDict) {
        sysDict.setDelFlag(2);
        sysDictService.updateById(sysDict);
        // 删除关联表中的数据
        String deleteId = sysDict.getId();
        LambdaQueryWrapper<SysDictItem> dictItemQuery = new LambdaQueryWrapper<SysDictItem>();
        dictItemQuery.eq(SysDictItem::getDictId, deleteId);
        boolean ok = sysDictItemService.remove(dictItemQuery);
        return ok;
    }

    @Override
    @Transactional
    public void saveMain(SysDict sysDict, List<SysDictItem> sysDictItemList) {

        sysDictMapper.insert(sysDict);
        if (sysDictItemList != null) {
            for (SysDictItem entity : sysDictItemList) {
                entity.setDictId(sysDict.getId());
                sysDictItemMapper.insert(entity);
            }
        }
    }

}
