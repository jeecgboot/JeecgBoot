package org.jeecg.modules.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.constant.DefIndexConst;
import org.jeecg.modules.system.entity.SysRoleIndex;
import org.jeecg.modules.system.mapper.SysRoleIndexMapper;
import org.jeecg.modules.system.service.ISysRoleIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
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
        queryWrapper.eq(SysRoleIndex::getStatus, CommonConstant.STATUS_1);
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
            entity.setRelationType(CommonConstant.HOME_RELATION_DEFAULT);
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
        entity.setRelationType(CommonConstant.HOME_RELATION_DEFAULT);
        return entity;
    }

    @Override
    public void cleanDefaultIndexCache() {
        redisUtil.del(DefIndexConst.CACHE_KEY + "::" + DefIndexConst.DEF_INDEX_ALL);
    }

    /**
     * 切换默认门户
     * @param sysRoleIndex
     */
    @Override
    public void changeDefHome(SysRoleIndex sysRoleIndex) {
        // 1. 先查询出配置信息
        String username = sysRoleIndex.getRoleCode();
        //当前状态(1:工作台/门户 0：菜单默认)
        String status = sysRoleIndex.getStatus();
        LambdaQueryWrapper<SysRoleIndex> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleIndex::getRoleCode, username);
        queryWrapper.eq(SysRoleIndex::getRelationType,CommonConstant.HOME_RELATION_USER);
        queryWrapper.orderByAsc(SysRoleIndex::getPriority);
        List<SysRoleIndex> list = super.list(queryWrapper);
        boolean success = false;
        if(CommonConstant.STATUS_1.equalsIgnoreCase(status)){
            // 2. 如果存在则编辑
            if (!CollectionUtils.isEmpty(list)) {
                sysRoleIndex.setId(list.get(0).getId());
                sysRoleIndex.setStatus(CommonConstant.STATUS_1);
                sysRoleIndex.setRoute(true);
                success = super.updateById(sysRoleIndex);
            } else {
                // 3. 如果不存在则新增
                sysRoleIndex.setRelationType(CommonConstant.HOME_RELATION_USER);
                sysRoleIndex.setStatus(CommonConstant.STATUS_1);
                sysRoleIndex.setRoute(true);
                success = super.save(sysRoleIndex);
            }
        }else {
            // 0：菜单默认，则是菜单默认首页
            if (!CollectionUtils.isEmpty(list)) {
                //将用户级别的首页配置状态设置成0
                for (int i = 0; i < list.size(); i++) {
                    SysRoleIndex roleIndex = list.get(i);
                    roleIndex.setStatus(CommonConstant.STATUS_0);
                    success = super.updateById(roleIndex);
                }
            }
        }
        // 4. 清理缓存
        if (success) {
            this.cleanDefaultIndexCache();
            redisUtil.del(DefIndexConst.CACHE_TYPE + username);
        }
        // 5. 缓存类型
        //当前地址
        String url = sysRoleIndex.getUrl();
        //首页类型(默认首页)
        String type = DefIndexConst.HOME_TYPE_MENU;
        if(oConvertUtils.isNotEmpty(url) && CommonConstant.STATUS_1.equalsIgnoreCase(status)){
           type = url.contains(DefIndexConst.HOME_TYPE_SYSTEM) ? DefIndexConst.HOME_TYPE_SYSTEM : DefIndexConst.HOME_TYPE_PERSONAL;
        }
        redisUtil.set(DefIndexConst.CACHE_TYPE + username,type);
    }

    /**
     * 更新其他全局默认的状态值
     *
     * @param roleCode
     * @param status
     * @param id
     */
    @Override
    public void updateOtherDefaultStatus(String roleCode, String status, String id) {
        //roleCode是全局默认
        if(oConvertUtils.isNotEmpty(roleCode) && DefIndexConst.DEF_INDEX_ALL.equals(roleCode)){
            //状态为开启状态
            if(oConvertUtils.isNotEmpty(status) && CommonConstant.STATUS_1.equals(status)){
                LambdaQueryWrapper<SysRoleIndex> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(SysRoleIndex::getRoleCode,roleCode);
                queryWrapper.eq(SysRoleIndex::getStatus,CommonConstant.STATUS_1);
                queryWrapper.ne(SysRoleIndex::getId,id);
                queryWrapper.select(SysRoleIndex::getId);
                List<SysRoleIndex> list = this.list(queryWrapper);
                if(CollectionUtil.isNotEmpty(list)){
                    list.forEach(sysRoleIndex -> {
                        sysRoleIndex.setStatus(CommonConstant.STATUS_0);
                    });
                    this.updateBatchById(list);
                }
            }
        }
    }

}
