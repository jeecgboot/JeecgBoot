package org.jeecg.modules.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.base.BaseMap;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.GlobalConstants;
import org.jeecg.common.exception.JeecgBootException;
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
import java.util.regex.Pattern;

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
    /**
     * 路由 uri 允许的协议
     */
    private static final List<String> ROUTE_URI_SCHEMES = List.of("lb://", "lb:ws://", "lb:wss://", "http://", "https://", "ws://", "wss://", "forward:", "no://op");
    /**
     * 允许的 SpEL 写法：仅 Bean 引用，如限流过滤器的 #{@ipKeyResolver}
     */
    private static final Pattern SPEL_BEAN_REF = Pattern.compile("#\\{@[A-Za-z_$][\\w$]*}");
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
        //update-begin---author:scott ---date:20261026  for：【GHSA-9h56-m6g6-7g8r】路由配置保存前校验：禁止在断言/过滤器参数中写 SpEL 表达式（仅放行 #{@beanName} 引用），uri 限定协议-----------
        JSONObject router = json.getJSONObject("router");
        if (router != null) {
            checkRouteConfig(router.getString("uri"), router.getString("predicates"), router.getString("filters"));
        }
        //update-end---author:scott ---date:20261026  for：【GHSA-9h56-m6g6-7g8r】路由配置保存前校验：禁止在断言/过滤器参数中写 SpEL 表达式（仅放行 #{@beanName} 引用），uri 限定协议-----------
        try {
            json = json.getJSONObject("router");
            String id = json.getString("id");
            // 代码逻辑说明: oracle路由网关新增小bug /issues/I4EV2J
            SysGatewayRoute route;
            if(oConvertUtils.isEmpty(id)){
                route = new SysGatewayRoute();
            }else{
                route = getById(id);
            }
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

    /**
     * 校验路由配置：Spring Cloud Gateway 会把 #{...} 形式的参数当作 SpEL 执行，uri 决定转发目标
     *
     * @param uri        路由地址
     * @param predicates 路由断言（JSON 数组字符串）
     * @param filters    过滤器（JSON 数组字符串）
     * @author scott
     * @since 2026-10-26 【GHSA-9h56-m6g6-7g8r】路由配置 SpEL 注入防护
     */
    private static void checkRouteConfig(String uri, String predicates, String filters) {
        if (oConvertUtils.isNotEmpty(uri)) {
            String lowerUri = uri.trim().toLowerCase();
            if (ROUTE_URI_SCHEMES.stream().noneMatch(lowerUri::startsWith)) {
                throw new JeecgBootException("路由地址只支持 lb://、http(s)://、ws(s)://、forward: 协议");
            }
        }
        checkNoSpel(predicates, "路由条件");
        checkNoSpel(filters, "过滤器");
    }

    /**
     * 断言/过滤器参数中禁止出现 SpEL 表达式（仅放行 #{@beanName} 引用）。
     * 先按 JSON 解析再序列化，避免用 Unicode 转义写法绕过
     *
     * @param text  JSON 数组字符串
     * @param label 提示名称
     * @author scott
     * @since 2026-10-26 【GHSA-9h56-m6g6-7g8r】路由配置 SpEL 注入防护
     */
    private static void checkNoSpel(String text, String label) {
        if (oConvertUtils.isEmpty(text)) {
            return;
        }
        String normalized;
        try {
            normalized = JSONArray.parseArray(text).toJSONString();
        } catch (Exception e) {
            throw new JeecgBootException(label + "格式错误");
        }
        if (SPEL_BEAN_REF.matcher(normalized).replaceAll("").contains("#{")) {
            throw new JeecgBootException(label + "参数不允许使用表达式 #{...}（仅支持 #{@beanName} 引用）");
        }
    }
}
