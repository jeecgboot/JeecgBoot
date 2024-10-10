package org.jeecg.modules.system.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysGatewayRoute;

import java.util.List;

/**
 * @Description: gateway路由管理
 * @Author: jeecg-boot
 * @Date:   2020-05-26
 * @Version: V1.0
 */
public interface ISysGatewayRouteService extends IService<SysGatewayRoute> {

    /**
     * 添加所有的路由信息到redis
     * @param key
     */
     void addRoute2Redis(String key);

    /**
     * 删除路由
     * @param id
     */
     void deleteById(String id);

    /**
     * 保存路由配置
     * @param array
     */
    void updateAll(JSONObject array);

    /**
     * 清空redis中的route信息
     */
    void clearRedis();

    /**
     * 还原逻辑删除
     * @param ids
     */
    void revertLogicDeleted(List<String> ids);

    /**
     * 彻底删除
     * @param ids
     */
    void deleteLogicDeleted(List<String> ids);

    /**
     * 复制路由
     * @param id
     * @return
     */
    SysGatewayRoute copyRoute(String id);

    /**
     * 获取删除列表
     * @return
     */
    List<SysGatewayRoute> getDeletelist();
}
