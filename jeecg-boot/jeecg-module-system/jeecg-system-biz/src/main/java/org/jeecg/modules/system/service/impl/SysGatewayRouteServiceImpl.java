package org.jeecg.modules.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.base.BaseMap;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.GlobalConstants;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysGatewayRoute;
import org.jeecg.modules.system.mapper.SysGatewayRouteMapper;
import org.jeecg.modules.system.service.ISysGatewayRouteService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Description: gateway路由管理
 * @Author: jeecg-boot
 * @Date: 2020-05-26
 * @Version: V1.0
 */
@Service
@Slf4j
public class SysGatewayRouteServiceImpl extends ServiceImpl<SysGatewayRouteMapper, SysGatewayRoute> implements ISysGatewayRouteService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String STRING_STATUS = "status";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMdd");
    @Override
    public void addRoute2Redis(String key) {
        List<SysGatewayRoute> ls = this.list(new LambdaQueryWrapper<SysGatewayRoute>());
        redisTemplate.opsForValue().set(key, JSON.toJSONString(ls));
    }

    @Override
    public void deleteById(String id) {
        //1.将状态修改成禁用
        SysGatewayRoute route = new SysGatewayRoute();
        route.setId(id);
        route.setStatus(0);
        this.baseMapper.updateById(route);
        this.removeById(id);
        //2.刷新路由
        this.resreshRouter(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAll(JSONObject json) {
        log.info("--gateway 路由配置修改--");
        try {
            json = json.getJSONObject("router");
            String id = json.getString("id");
            //update-begin-author:taoyan date:20211025 for: oracle路由网关新增小bug /issues/I4EV2J
            SysGatewayRoute route;
            if(oConvertUtils.isEmpty(id)){
                route = new SysGatewayRoute();
            }else{
                route = getById(id);
            }
            //update-end-author:taoyan date:20211025 for: oracle路由网关新增小bug /issues/I4EV2J
            if (ObjectUtil.isEmpty(route)) {
                route = new SysGatewayRoute();
            }
            route.setRouterId(json.getString("routerId"));
            route.setName(json.getString("name"));
            route.setPredicates(json.getString("predicates"));
            //初始化删除状态
            route.setDelFlag(CommonConstant.DEL_FLAG_0);
            String filters = json.getString("filters");
            if (ObjectUtil.isEmpty(filters)) {
                filters = "[]";
            }
            route.setFilters(filters);
            route.setUri(json.getString("uri"));
            if (json.get(STRING_STATUS) == null) {
                route.setStatus(1);
            } else {
                route.setStatus(json.getInteger(STRING_STATUS));
            }
            this.saveOrUpdate(route);
            resreshRouter(null);
        } catch (Exception e) {
            log.error("路由配置解析失败", e);
            resreshRouter(null);
            e.printStackTrace();
        }
    }

    /**
     * 更新redis路由缓存
     */
    private void resreshRouter(String delRouterId) {
        //更新redis路由缓存
        addRoute2Redis(CacheConstant.GATEWAY_ROUTES);
        BaseMap params = new BaseMap();
        params.put(GlobalConstants.HANDLER_NAME, GlobalConstants.LODER_ROUDER_HANDLER);
        params.put("delRouterId", delRouterId);
        //刷新网关
        redisTemplate.convertAndSend(GlobalConstants.REDIS_TOPIC_NAME, params);
    }

    @Override
    public void clearRedis() {
        redisTemplate.opsForValue().set(CacheConstant.GATEWAY_ROUTES, null);
    }

    /**
     * 还原逻辑删除
     * @param ids
     */
    @Override
    public void revertLogicDeleted(List<String> ids) {
        this.baseMapper.revertLogicDeleted(ids);
        resreshRouter(null);
    }

    /**
     * 彻底删除
     * @param ids
     */
    @Override
    public void deleteLogicDeleted(List<String> ids) {
        this.baseMapper.deleteLogicDeleted(ids);
        resreshRouter(ids.get(0));
    }

    /**
     * 路由复制
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysGatewayRoute copyRoute(String id) {
        log.info("--gateway 路由复制--");
        SysGatewayRoute targetRoute = new SysGatewayRoute();
        try {
            SysGatewayRoute sourceRoute = this.baseMapper.selectById(id);
            //1.复制路由
            BeanUtils.copyProperties(sourceRoute,targetRoute);
            //1.1 获取当前日期
            String formattedDate = dateFormat.format(new Date());
            String copyRouteName = sourceRoute.getName() + "_copy_";
            //1.2 判断数据库是否存在
            Long count = this.baseMapper.selectCount(new LambdaQueryWrapper<SysGatewayRoute>().eq(SysGatewayRoute::getName, copyRouteName + formattedDate));
            //1.3 新的路由名称
            copyRouteName +=  count > 0?RandomUtil.randomNumbers(4):formattedDate;

            targetRoute.setId(null);
            targetRoute.setName(copyRouteName);
            targetRoute.setCreateTime(new Date());
            targetRoute.setStatus(0);
            targetRoute.setDelFlag(CommonConstant.DEL_FLAG_0);
            this.baseMapper.insert(targetRoute);
            //2.刷新路由
            resreshRouter(null);
        } catch (Exception e) {
            log.error("路由配置解析失败", e);
            resreshRouter(null);
            e.printStackTrace();
        }
        return targetRoute;
    }

    /**
     * 查询删除列表
     * @return
     */
    @Override
    public List<SysGatewayRoute> getDeletelist() {
        return baseMapper.queryDeleteList();
    }
}
