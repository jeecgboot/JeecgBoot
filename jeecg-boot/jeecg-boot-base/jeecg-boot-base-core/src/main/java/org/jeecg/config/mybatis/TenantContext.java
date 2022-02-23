package org.jeecg.config.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.vo.TenantVo;

/**
 * 多租户 tenant_id存储器
 */
@Slf4j
public class TenantContext {

    private static ThreadLocal<TenantVo> currentTenant = new ThreadLocal<>();

    public static void setTenant(TenantVo tenant) {
        log.debug(" setting tenant to " + tenant);
        currentTenant.set(tenant);
//        log.info("set tenant:{},thread:{}",tenant,Thread.currentThread().getName());
    }


    public static TenantVo getTenant() {
        return currentTenant.get();
    }

    public static String getTenantId() {
        TenantVo v = currentTenant.get();
        if(v != null){
            return v.getTenantId();
        }
        return null;
    }

    public static String getTenantIds() {
        TenantVo v = currentTenant.get();
        if(v != null){
            return v.getTenantIds();
        }
        return null;
    }

    public static boolean canQueryAll() {
        TenantVo v = currentTenant.get();
        if(v != null){
            return v.isQueryAll();
        }
        return false;
    }

    public static void clear(){
        currentTenant.remove();
    }
}
