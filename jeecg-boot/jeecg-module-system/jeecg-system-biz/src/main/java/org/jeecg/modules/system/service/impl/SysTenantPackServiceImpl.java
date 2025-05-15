package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.constant.TenantConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.aop.TenantLog;
import org.jeecg.modules.system.entity.SysPackPermission;
import org.jeecg.modules.system.entity.SysTenant;
import org.jeecg.modules.system.entity.SysTenantPack;
import org.jeecg.modules.system.entity.SysTenantPackUser;
import org.jeecg.modules.system.mapper.SysPackPermissionMapper;
import org.jeecg.modules.system.mapper.SysRoleMapper;
import org.jeecg.modules.system.mapper.SysTenantPackMapper;
import org.jeecg.modules.system.mapper.SysTenantPackUserMapper;
import org.jeecg.modules.system.service.ISysTenantPackService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 租户产品包
 * @Author: jeecg-boot
 * @Date: 2022-12-31
 * @Version: V1.0
 */
@Service
public class SysTenantPackServiceImpl extends ServiceImpl<SysTenantPackMapper, SysTenantPack> implements ISysTenantPackService {

    @Autowired
    private SysTenantPackMapper sysTenantPackMapper;

    @Autowired
    private SysTenantPackUserMapper sysTenantPackUserMapper;

    @Autowired
    private SysPackPermissionMapper sysPackPermissionMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public void addPackPermission(SysTenantPack sysTenantPack) {
        sysTenantPackMapper.insert(sysTenantPack);
        String permissionIds = sysTenantPack.getPermissionIds();
        if (oConvertUtils.isNotEmpty(permissionIds)) {
            String packId = sysTenantPack.getId();
            String[] permissionIdArray = permissionIds.split(SymbolConstant.COMMA);
            for (String permissionId : permissionIdArray) {
                this.addPermission(packId, permissionId);
            }
        }

    }

    @Override
    public List<SysTenantPack> setPermissions(List<SysTenantPack> records) {
        for (SysTenantPack pack : records) {
            List<String> permissionIds = sysPackPermissionMapper.getPermissionsByPackId(pack.getId());
            if (null != permissionIds && permissionIds.size() > 0) {
                String ids = String.join(SymbolConstant.COMMA, permissionIds);
                pack.setPermissionIds(ids);
            }
        }
        return records;
    }

    @Override
    public void editPackPermission(SysTenantPack sysTenantPack) {
        //数据库汇总的id
        List<String> oldPermissionIds = sysPackPermissionMapper.getPermissionsByPackId(sysTenantPack.getId());
        //前台传过来的需要修改的id
        String permissionIds = sysTenantPack.getPermissionIds();
        //如果传过来的菜单id为空，那么就删除数据库中所有菜单
        if (oConvertUtils.isEmpty(permissionIds)) {
            this.deletePackPermission(sysTenantPack.getId(), null);
        } else if (oConvertUtils.isNotEmpty(permissionIds) && oConvertUtils.isEmpty(oldPermissionIds)) {
            //如果传过来的菜单id不为空但是数据库的菜单id为空，那么就新增
            this.addPermission(sysTenantPack.getId(), permissionIds);
        } else {
            //都不为空，需要比较，进行添加或删除
            if (oConvertUtils.isNotEmpty(oldPermissionIds)) {
                //找到新的租户id与原来的租户id不同之处，进行删除
                List<String> permissionList = oldPermissionIds.stream().filter(item -> !permissionIds.contains(item)).collect(Collectors.toList());
                if (permissionList.size() > 0) {
                    for (String permission : permissionList) {
                        this.deletePackPermission(sysTenantPack.getId(), permission);
                    }
                }

                //找到原来菜单id与新的菜单id不同之处,进行新增
                List<String> permissionAddList = Arrays.stream(permissionIds.split(SymbolConstant.COMMA)).filter(item -> !oldPermissionIds.contains(item)).collect(Collectors.toList());
                if (permissionAddList.size() > 0) {
                    for (String permission : permissionAddList) {
                        this.addPermission(sysTenantPack.getId(), permission);
                    }
                }
            }
        }
        sysTenantPackMapper.updateById(sysTenantPack);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTenantPack(String ids) {
        String[] idsArray = ids.split(SymbolConstant.COMMA);
        for (String id : idsArray) {
            this.deletePackPermission(id,null);
            //删除产品包下面的用户
            this.deletePackUser(id);
            sysTenantPackMapper.deleteById(id);
        }
    }

    @Override
    public void exitTenant(String tenantId, String userId) {
        this.getById(tenantId);
    }

    @Override
    public void addDefaultTenantPack(Integer tenantId) {
        ISysTenantPackService currentService = SpringContextUtils.getApplicationContext().getBean(ISysTenantPackService.class);
        // 创建租户超级管理员
        SysTenantPack superAdminPack = new SysTenantPack(tenantId, "超级管理员", TenantConstant.SUPER_ADMIN);
        
        //step.1 创建租户套餐包（超级管理员）
        LambdaQueryWrapper<SysTenantPack> query = new LambdaQueryWrapper<>();
        query.eq(SysTenantPack::getTenantId,tenantId);
        query.eq(SysTenantPack::getPackCode, TenantConstant.SUPER_ADMIN);
        SysTenantPack sysTenantPackSuperAdmin = currentService.getOne(query);
        String packId = "";
        if(null == sysTenantPackSuperAdmin){
            packId = currentService.saveOne(superAdminPack);
        }else{
            packId = sysTenantPackSuperAdmin.getId();
        }
        //step.1.2 补充人员与套餐包的关系数据
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        SysTenantPackUser packUser = new SysTenantPackUser(tenantId, packId, sysUser.getId());
        packUser.setRealname(sysUser.getRealname());
        packUser.setPackName(superAdminPack.getPackName());
        currentService.savePackUser(packUser);
        
        //step.2 创建租户套餐包(组织账户管理员)和 添加人员关系数据
        query.eq(SysTenantPack::getTenantId,tenantId);
        query.eq(SysTenantPack::getPackCode, TenantConstant.ACCOUNT_ADMIN);
        SysTenantPack sysTenantPackAccountAdmin = currentService.getOne(query);
        if(null == sysTenantPackAccountAdmin){
            // 创建超级管理员
            SysTenantPack accountAdminPack = new SysTenantPack(tenantId, "组织账户管理员", TenantConstant.ACCOUNT_ADMIN);
            currentService.saveOne(accountAdminPack);
        }

        //step.3 创建租户套餐包(组织应用管理员)
        query.eq(SysTenantPack::getTenantId,tenantId);
        query.eq(SysTenantPack::getPackCode, TenantConstant.APP_ADMIN);
        SysTenantPack sysTenantPackAppAdmin = currentService.getOne(query);
        if(null == sysTenantPackAppAdmin){
            // 创建超级管理员
            SysTenantPack appAdminPack = new SysTenantPack(tenantId, "组织应用管理员", TenantConstant.APP_ADMIN);
            currentService.saveOne(appAdminPack);
        }
        
    }

    @TenantLog(2)
    @Override
    public String saveOne(SysTenantPack sysTenantPack) {
        sysTenantPackMapper.insert(sysTenantPack);
        return sysTenantPack.getId();
    }

    @TenantLog(2)
    @Override
    public void savePackUser(SysTenantPackUser sysTenantPackUser) {
        sysTenantPackUser.setStatus(1);
        sysTenantPackUserMapper.insert(sysTenantPackUser);
    }

    @Override
    public SysTenantPack getSysTenantPack(Integer tenantId, String packCode) {
        LambdaQueryWrapper<SysTenantPack> query = new LambdaQueryWrapper<SysTenantPack>()
                .eq(SysTenantPack::getPackCode, packCode)
                .eq(SysTenantPack::getTenantId, tenantId);
        List<SysTenantPack> list = baseMapper.selectList(query);
        if(list!=null && list.size()>0){
            SysTenantPack pack = list.get(0);
            if(pack!=null && pack.getId()!=null){
                return pack;
            }
        }
        return null;
    }

    /**
     * 添加菜单
     *
     * @param packId
     * @param permissionId
     */
    public void addPermission(String packId, String permissionId) {
        SysPackPermission permission = new SysPackPermission();
        permission.setPermissionId(permissionId);
        permission.setPackId(packId);
        sysPackPermissionMapper.insert(permission);
    }

    /**
     * 根据包名id和菜单id删除关系表
     *
     * @param packId
     * @param permissionId
     */
    public void deletePackPermission(String packId, String permissionId) {
        LambdaQueryWrapper<SysPackPermission> query = new LambdaQueryWrapper<>();
        query.eq(SysPackPermission::getPackId, packId);
        if (oConvertUtils.isNotEmpty(permissionId)) {
            query.eq(SysPackPermission::getPermissionId, permissionId);
        }
        sysPackPermissionMapper.delete(query);
    }

    @Override
    public void addTenantDefaultPack(Integer tenantId) {
        LambdaQueryWrapper<SysTenantPack> query = new LambdaQueryWrapper<>();
        query.eq(SysTenantPack::getPackType,"default");
        List<SysTenantPack> sysTenantPacks = sysTenantPackMapper.selectList(query);
        for (SysTenantPack sysTenantPack: sysTenantPacks) {
            syncDefaultPack2CurrentTenant(tenantId, sysTenantPack);
        }
    }

    @Override
    public void syncDefaultPack(Integer tenantId) {
        // 查询默认套餐包
        LambdaQueryWrapper<SysTenantPack> query = new LambdaQueryWrapper<>();
        query.eq(SysTenantPack::getPackType,"default");
        List<SysTenantPack> sysDefaultTenantPacks = sysTenantPackMapper.selectList(query);
        // 查询当前租户套餐包
        query = new LambdaQueryWrapper<>();
        query.eq(SysTenantPack::getPackType,"custom");
        query.eq(SysTenantPack::getTenantId, tenantId);
        List<SysTenantPack> currentTenantPacks = sysTenantPackMapper.selectList(query);
        Map<String, SysTenantPack> currentTenantPackMap = new HashMap<String, SysTenantPack>();
        if (oConvertUtils.listIsNotEmpty(currentTenantPacks)) {
            currentTenantPackMap = currentTenantPacks.stream().collect(Collectors.toMap(SysTenantPack::getPackName, o -> o, (existing, replacement) -> existing));
        }
        // 添加不存在的套餐包
        for (SysTenantPack defaultPacks : sysDefaultTenantPacks) {
            if(!currentTenantPackMap.containsKey(defaultPacks.getPackName())){
                syncDefaultPack2CurrentTenant(tenantId, defaultPacks);
            }
        }
    }

    /**
     * 同步默认套餐包到当前租户
     * for [QQYUN-11032]【jeecg】租户套餐管理增加初始化套餐包按钮
     * @param tenantId 目标租户
     * @param defaultPacks 默认套餐包
     * @author chenrui
     * @date 2025/2/5 19:41
     */
    private void syncDefaultPack2CurrentTenant(Integer tenantId, SysTenantPack defaultPacks) {
        SysTenantPack pack = new SysTenantPack();
        BeanUtils.copyProperties(defaultPacks,pack);
        pack.setTenantId(tenantId);
        pack.setPackType("custom");
        pack.setId("");
        sysTenantPackMapper.insert(pack);
        List<String> permissionsByPackId = sysPackPermissionMapper.getPermissionsByPackId(defaultPacks.getId());
        for (String permission:permissionsByPackId) {
            SysPackPermission packPermission = new SysPackPermission();
            packPermission.setPackId(pack.getId());
            packPermission.setPermissionId(permission);
            sysPackPermissionMapper.insert(packPermission);
        }
    }

    /**
     * 删除产品包下面的用户
     * @param packId
     */
    private void deletePackUser(String packId) {
        LambdaQueryWrapper<SysTenantPackUser> query = new LambdaQueryWrapper<>();
        query.eq(SysTenantPackUser::getPackId, packId);
        sysTenantPackUserMapper.delete(query);
    }
}
