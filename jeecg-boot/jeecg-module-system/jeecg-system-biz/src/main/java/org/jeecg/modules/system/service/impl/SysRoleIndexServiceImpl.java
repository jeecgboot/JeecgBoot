package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.system.constant.DefIndexConst;
import org.jeecg.modules.system.entity.SysRoleIndex;
import org.jeecg.modules.system.mapper.SysRoleIndexMapper;
import org.jeecg.modules.system.service.ISysRoleIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @Description: 角色首页配置
 * @Author: jeecg-boot
 * @Date: 2022-03-25
 * @Version: V1.0
 */
@Service("sysRoleIndexServiceImpl")
public class SysRoleIndexServiceImpl extends ServiceImpl<SysRoleIndexMapper, SysRoleIndex> implements ISysRoleIndexService {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    @Cacheable(cacheNames = DefIndexConst.CACHE_KEY, key = "'" + DefIndexConst.DEF_INDEX_ALL + "'")
    public SysRoleIndex queryDefaultIndex() {
        LambdaQueryWrapper<SysRoleIndex> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleIndex::getRoleCode, DefIndexConst.DEF_INDEX_ALL);
        SysRoleIndex entity = super.getOne(queryWrapper);
        // 保证不为空
        if (entity == null) {
            entity = this.initDefaultIndex();
        }
        return entity;
    }

    @Override
    public boolean updateDefaultIndex(String url, String component, boolean isRoute) {
        // 1. 先查询出配置信息
        LambdaQueryWrapper<SysRoleIndex> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleIndex::getRoleCode, DefIndexConst.DEF_INDEX_ALL);
        SysRoleIndex entity = super.getOne(queryWrapper);
        boolean success = false;
        // 2. 如果不存在则新增
        if (entity == null) {
            entity = this.newDefIndexConfig(url, component, isRoute);
            success = super.save(entity);
        } else {
            // 3. 如果存在则更新
            entity.setUrl(url);
            entity.setComponent(component);
            entity.setRoute(isRoute);
            success = super.updateById(entity);
        }
        // 4. 清理缓存
        if (success) {
            this.cleanDefaultIndexCache();
        }
        return success;
    }

    @Override
    public SysRoleIndex initDefaultIndex() {
        return this.newDefIndexConfig(DefIndexConst.DEF_INDEX_URL, DefIndexConst.DEF_INDEX_COMPONENT, true);
    }

    /**
     * 创建默认首页配置
     *
     * @param indexComponent
     * @return
     */
    private SysRoleIndex newDefIndexConfig(String indexUrl, String indexComponent, boolean isRoute) {
        SysRoleIndex entity = new SysRoleIndex();
        entity.setRoleCode(DefIndexConst.DEF_INDEX_ALL);
        entity.setUrl(indexUrl);
        entity.setComponent(indexComponent);
        entity.setRoute(isRoute);
        entity.setStatus(CommonConstant.STATUS_1);
        return entity;
    }

    @Override
    public void cleanDefaultIndexCache() {
        redisUtil.del(DefIndexConst.CACHE_KEY + "::" + DefIndexConst.DEF_INDEX_ALL);
    }

}
