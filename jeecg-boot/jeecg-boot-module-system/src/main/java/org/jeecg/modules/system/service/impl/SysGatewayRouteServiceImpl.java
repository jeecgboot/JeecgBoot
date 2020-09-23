package org.jeecg.modules.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.modules.system.entity.SysGatewayRoute;
import org.jeecg.modules.system.mapper.SysGatewayRouteMapper;
import org.jeecg.modules.system.service.ISysGatewayRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: gateway路由管理
 * @Author: jeecg-boot
 * @Date:   2020-05-26
 * @Version: V1.0
 */
@Service
@Slf4j
public class SysGatewayRouteServiceImpl extends ServiceImpl<SysGatewayRouteMapper, SysGatewayRoute> implements ApplicationEventPublisherAware,ISysGatewayRouteService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void addRoute2Redis(String key) {
        List<SysGatewayRoute> ls = this.list(new LambdaQueryWrapper<SysGatewayRoute>().eq(SysGatewayRoute::getStatus,1));
        redisTemplate.opsForValue().set(key, JSON.toJSONString(ls));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAll(JSONArray array) {
        log.info("--gateway 路由配置修改--");
        try {
            this.remove(new LambdaQueryWrapper<SysGatewayRoute>().eq(SysGatewayRoute::getStatus,1));
            List<SysGatewayRoute> ls = new ArrayList<>();
            for(int i =0;i<array.size();i++){
                JSONObject json = array.getJSONObject(i);
                SysGatewayRoute route = new SysGatewayRoute();
                route.setId(json.getString("id"));
                route.setName(json.getString("name"));
                route.setPredicates(json.getString("predicates"));
                route.setFilters(json.getString("filters"));
                route.setUri(json.getString("uri"));
                if(json.get("status")==null){
                    route.setStatus(1);
                }else{
                    route.setStatus(json.getInteger("status"));
                }
                ls.add(route);
            }
            this.saveBatch(ls);
            redisTemplate.opsForValue().set(CacheConstant.GATEWAY_ROUTES,  JSON.toJSONString(ls));
            //执行完毕需要重新加载路由 但是触发事件在另一个项目，此路不通，只能通过redis监听整了
            // this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
            redisTemplate.convertAndSend(CacheConstant.ROUTE_JVM_RELOAD_TOPIC, "system路由信息,网关缓存更新>>");
        } catch (Exception e) {
            log.error("路由配置解析失败", e);
            //报错需要重新初始化路由
            //this.applicationEventPublisher.publishEvent(new GatewayRouteInitEvent(this));
            e.printStackTrace();
        }
    }

    @Override
    public void clearRedis() {
        redisTemplate.opsForValue().set(CacheConstant.GATEWAY_ROUTES,  null);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
