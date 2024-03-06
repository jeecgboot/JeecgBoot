package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysRoleIndex;

/**
 * @Description: 角色首页配置
 * @Author: jeecg-boot
 * @Date: 2022-03-25
 * @Version: V1.0
 */
public interface ISysRoleIndexService extends IService<SysRoleIndex> {

    /**
     * 查询默认首页
     *
     * @return
     */
    SysRoleIndex queryDefaultIndex();

    /**
     * 更新默认首页
     *
     * @param url
     * @param component
     * @param isRoute   是否是路由页面
     * @return
     */
    boolean updateDefaultIndex(String url, String component, boolean isRoute);

    /**
     * 创建最原始的默认首页配置
     *
     * @return
     */
    SysRoleIndex initDefaultIndex();

    /**
     * 清理默认首页的redis缓存
     */
    void cleanDefaultIndexCache();

}
