package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysDepartPermission;
import org.jeecg.modules.system.entity.SysDepartRole;
import org.jeecg.modules.system.entity.SysDepartRolePermission;
import org.jeecg.modules.system.entity.SysPermissionDataRule;
import org.jeecg.modules.system.mapper.SysDepartPermissionMapper;
import org.jeecg.modules.system.mapper.SysDepartRoleMapper;
import org.jeecg.modules.system.mapper.SysDepartRolePermissionMapper;
import org.jeecg.modules.system.mapper.SysPermissionDataRuleMapper;
import org.jeecg.modules.system.service.ISysDepartPermissionService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 部门权限表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Service
public class SysDepartPermissionServiceImpl extends ServiceImpl<SysDepartPermissionMapper, SysDepartPermission> implements ISysDepartPermissionService {
    @Resource
    private SysPermissionDataRuleMapper ruleMapper;

    @Resource
    private SysDepartRoleMapper sysDepartRoleMapper;

    @Resource
    private SysDepartRolePermissionMapper departRolePermissionMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDepartPermission(String departId, String permissionIds, String lastPermissionIds) {
        //1.对比要新增的权限
        List<String> add = getDiff(lastPermissionIds,permissionIds);
        if(add!=null && add.size()>0) {
            List<SysDepartPermission> list = new ArrayList<SysDepartPermission>();
            for (String p : add) {
                if(oConvertUtils.isNotEmpty(p)) {
                    SysDepartPermission rolepms = new SysDepartPermission(departId, p);
                    list.add(rolepms);
                }
            }
            this.saveBatch(list);
        }
        //2.对比要删除的权限
        List<String> delete = getDiff(permissionIds,lastPermissionIds);
        if(delete!=null && delete.size()>0) {
            for (String permissionId : delete) {
                //2.1 删除部门对应的权限
                this.remove(new QueryWrapper<SysDepartPermission>().lambda()
                        .eq(SysDepartPermission::getDepartId, departId)
                        .eq(SysDepartPermission::getPermissionId, permissionId));
                //2.2 删除部门权限时，删除部门角色中已授权的权限
                List<SysDepartRole> sysDepartRoleList = sysDepartRoleMapper.selectList(new LambdaQueryWrapper<SysDepartRole>().eq(SysDepartRole::getDepartId,departId));
                List<String> roleIds = sysDepartRoleList.stream().map(SysDepartRole::getId).collect(Collectors.toList());
                if(roleIds != null && roleIds.size()>0){
                    departRolePermissionMapper.delete(new LambdaQueryWrapper<SysDepartRolePermission>()
                            .eq(SysDepartRolePermission::getPermissionId,permissionId)
                        //update-begin-author:liusq---date:2023-10-08--for: [issue/#5339]部门管理下部门赋权代码逻辑缺少判断条件
                            .in(SysDepartRolePermission::getRoleId,roleIds)
                        //update-end-author:liusq---date:2023-10-08--for: [issue/#5339]部门管理下部门赋权代码逻辑缺少判断条件
                    );
                }
            }
        }
    }

    @Override
    public List<SysPermissionDataRule> getPermRuleListByDeptIdAndPermId(String departId, String permissionId) {
        SysDepartPermission departPermission = this.getOne(new QueryWrapper<SysDepartPermission>().lambda().eq(SysDepartPermission::getDepartId, departId).eq(SysDepartPermission::getPermissionId, permissionId));
        if(departPermission != null && oConvertUtils.isNotEmpty(departPermission.getDataRuleIds())){
            LambdaQueryWrapper<SysPermissionDataRule> query = new LambdaQueryWrapper<SysPermissionDataRule>();
            query.in(SysPermissionDataRule::getId, Arrays.asList(departPermission.getDataRuleIds().split(",")));
            query.orderByDesc(SysPermissionDataRule::getCreateTime);
            List<SysPermissionDataRule> permRuleList = this.ruleMapper.selectList(query);
            return permRuleList;
        }else{
            return null;
        }
    }

    /**
     * 从diff中找出main中没有的元素
     * @param main
     * @param diff
     * @return
     */
    private List<String> getDiff(String main,String diff){
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
