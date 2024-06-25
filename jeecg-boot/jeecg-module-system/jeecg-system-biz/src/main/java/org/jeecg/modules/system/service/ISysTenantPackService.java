package org.jeecg.modules.system.service;

import org.jeecg.modules.system.entity.SysTenantPack;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysTenantPackUser;

import java.util.List;

/**
 * @Description: 租户产品包
 * @Author: jeecg-boot
 * @Date:   2022-12-31
 * @Version: V1.0
 */
public interface ISysTenantPackService extends IService<SysTenantPack> {

    /**
     * 新增产品包并将菜单插入关系表
     * @param sysTenantPack
     */
    void addPackPermission(SysTenantPack sysTenantPack);

    /**
     * 设置菜单id
     * @param records
     * @return
     */
    List<SysTenantPack> setPermissions(List<SysTenantPack> records);

    /**
     * 编辑产品包并将菜单插入关系表
     * @param sysTenantPack
     */
    void editPackPermission(SysTenantPack sysTenantPack);

    /**
     * 删除租户产品包
     * @param ids
     */
    void deleteTenantPack(String ids);

    /**
     * 退出租户
     * @param tenantId
     * @param s
     */
    void exitTenant(String tenantId, String s);

    /**
     * 创建租户的时候默认创建3个 产品包
     * @param tenantId
     */
    void addDefaultTenantPack(Integer tenantId);

    /**
     * 保存产品包
     * @param sysTenantPack
     */
    String saveOne(SysTenantPack sysTenantPack);


    /**
     * 保存产品包和人员的关系
     * @param sysTenantPackUser
     */
    void savePackUser(SysTenantPackUser sysTenantPackUser);

    /**
     * 根据租户ID和编码查询
     * @param tenantId
     * @param packCode
     * @return
     */
    SysTenantPack getSysTenantPack(Integer tenantId ,String packCode);
   
    /**
     * 添加由管理员创建的默认产品包
     * @param id
     */
    void addTenantDefaultPack(Integer id);
}
