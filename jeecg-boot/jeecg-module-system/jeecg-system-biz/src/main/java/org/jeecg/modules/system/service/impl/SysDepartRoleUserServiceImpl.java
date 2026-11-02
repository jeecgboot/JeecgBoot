package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysDepartRole;
import org.jeecg.modules.system.entity.SysDepartRoleUser;
import org.jeecg.modules.system.mapper.SysDepartRoleMapper;
import org.jeecg.modules.system.mapper.SysDepartRoleUserMapper;
import org.jeecg.modules.system.service.ISysDepartRoleUserService;
import org.jeecg.modules.system.util.ShiroAuthCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 部门角色人员信息
 * @Author: jeecg-boot
 * @Date:   2020-02-13
 * @Version: V1.0
 */
@Service
public class SysDepartRoleUserServiceImpl extends ServiceImpl<SysDepartRoleUserMapper, SysDepartRoleUser> implements ISysDepartRoleUserService {
    @Autowired
    private SysDepartRoleMapper sysDepartRoleMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void deptRoleUserAdd(String userId, String newRoleId, String oldRoleId) {
        List<String> add = getDiff(oldRoleId,newRoleId);
        if(add!=null && add.size()>0) {
            List<SysDepartRoleUser> list = new ArrayList<>();
            for (String roleId : add) {
                if(oConvertUtils.isNotEmpty(roleId)) {
                    SysDepartRoleUser rolepms = new SysDepartRoleUser(userId, roleId);
                    list.add(rolepms);
                }
            }
            this.saveBatch(list);
        }
        List<String> remove = getDiff(newRoleId,oldRoleId);
        if(remove!=null && remove.size()>0) {
            for (String roleId : remove) {
                this.remove(new QueryWrapper<SysDepartRoleUser>().lambda().eq(SysDepartRoleUser::getUserId, userId).eq(SysDepartRoleUser::getDroleId, roleId));
            }
        }
        //update-begin---author:scott ---date:20261029  for：【LHZP-1129】部门角色授权后清理相关用户Shiro权限缓存，无需重新登录即生效-----------
        ShiroAuthCacheUtil.clearUserAuthCache(redisUtil, Collections.singletonList(userId));
        //update-end---author:scott ---date:20261029  for：【LHZP-1129】部门角色授权后清理相关用户Shiro权限缓存，无需重新登录即生效-----------
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeDeptRoleUser(List<String> userIds, String depId) {
        for(String userId : userIds){
            List<SysDepartRole> sysDepartRoleList = sysDepartRoleMapper.selectList(new QueryWrapper<SysDepartRole>().eq("depart_id",depId));
            List<String> roleIds = sysDepartRoleList.stream().map(SysDepartRole::getId).collect(Collectors.toList());
            if(roleIds != null && roleIds.size()>0){
                QueryWrapper<SysDepartRoleUser> query = new QueryWrapper<>();
                query.eq("user_id",userId).in("drole_id",roleIds);
                this.remove(query);
            }
        }
        //update-begin---author:scott ---date:20261029  for：【LHZP-1129】部门角色授权后清理相关用户Shiro权限缓存，无需重新登录即生效-----------
        ShiroAuthCacheUtil.clearUserAuthCache(redisUtil, userIds);
        //update-end---author:scott ---date:20261029  for：【LHZP-1129】部门角色授权后清理相关用户Shiro权限缓存，无需重新登录即生效-----------
    }

    /**
     * 清理部门角色下所有用户的Shiro权限缓存
     * @param roleId 部门角色id
     * @author scott
     * @since 2026-10-29 【LHZP-1129】部门角色授权后清理相关用户权限缓存，无需重新登录即生效
     */
    @Override
    public void clearDeptRoleUserAuthCache(String roleId) {
        if (oConvertUtils.isEmpty(roleId)) {
            return;
        }
        List<SysDepartRoleUser> roleUsers = this.list(new QueryWrapper<SysDepartRoleUser>().lambda().select(SysDepartRoleUser::getUserId).eq(SysDepartRoleUser::getDroleId, roleId));
        ShiroAuthCacheUtil.clearUserAuthCache(redisUtil, roleUsers.stream().map(SysDepartRoleUser::getUserId).collect(Collectors.toSet()));
    }

    /**
     * 从diff中找出main中没有的元素
     * @param main
     * @param diff
     * @return
     */
    private List<String> getDiff(String main, String diff){
        if(oConvertUtils.isEmpty(diff)) {
            return null;
        }
        if(oConvertUtils.isEmpty(main)) {
            return Arrays.asList(diff.split(","));
        }

        String[] mainArr = main.split(",");
        String[] diffArr = diff.split(",");
        Map<String, Integer> map = new HashMap(5);
        for (String string : mainArr) {
            map.put(string, 1);
        }
        List<String> res = new ArrayList<String>();
        for (String key : diffArr) {
            if(oConvertUtils.isNotEmpty(key) && !map.containsKey(key)) {
                res.add(key);
            }
        }
        return res;
    }
}
