package org.jeecg.loader;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.base.BaseMap;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.config.GatewayRoutersConfig;
import org.jeecg.config.RouterDataType;
import org.jeecg.loader.repository.DynamicRouteService;
import org.jeecg.loader.repository.MyInMemoryRouteDefinitionRepository;
import org.jeecg.loader.vo.MyRouteDefinition;
import org.jeecg.loader.vo.PredicatesVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.Executor;

/**
 * 动态路由加载器
 *
 * @author : zyf
 * @date :2020-11-10
 */
@Slf4j
@Component
@RefreshScope
@DependsOn({"gatewayRoutersConfig"})
public class DynamicRouteLoader implements ApplicationEventPublisherAware {

    public static final long DEFAULT_TIMEOUT = 30000;
    @Autowired
    private GatewayRoutersConfig gatewayRoutersConfig;
    private MyInMemoryRouteDefinitionRepository repository;
    private ApplicationEventPublisher publisher;
    private DynamicRouteService dynamicRouteService;
    private ConfigService configService;
    private RedisUtil redisUtil;


    /**
     * 需要拼接key的路由条件
     */
    private static String[] GEN_KEY_ROUTERS = new String[]{"Path", "Host", "Method", "After", "Before", "Between", "RemoteAddr"};

    public DynamicRouteLoader(MyInMemoryRouteDefinitionRepository repository, DynamicRouteService dynamicRouteService, RedisUtil redisUtil) {

        this.repository = repository;
        this.dynamicRouteService = dynamicRouteService;
        this.redisUtil = redisUtil;
    }

//    @PostConstruct
//    public void init() {
//       init(null);
//    }


    public void init(BaseMap baseMap) {
        log.info("初始化路由模式，dataType："+ gatewayRoutersConfig.getDataType());
        if (RouterDataType.nacos.toString().endsWith(gatewayRoutersConfig.getDataType())) {
            loadRoutesByNacos();
        }
        //从数据库加载路由
        if (RouterDataType.database.toString().endsWith(gatewayRoutersConfig.getDataType())) {
            loadRoutesByRedis(baseMap);
        }
    }
    /**
     * 刷新路由
     *
     * @return
     */
    public Mono<Void> refresh(BaseMap baseMap) {
        log.info("初始化路由模式，dataType："+ gatewayRoutersConfig.getDataType());
        if (!RouterDataType.yml.toString().endsWith(gatewayRoutersConfig.getDataType())) {
            this.init(baseMap);
        }
        return Mono.empty();
    }


    /**
     * 从nacos中读取路由配置
     *
     * @return
     */
    private void loadRoutesByNacos() {
        List<RouteDefinition> routes = Lists.newArrayList();
        configService = createConfigService();
        if (configService == null) {
            log.warn("initConfigService fail");
        }
        try {
            log.info("jeecg.route.config.data-id = {}", gatewayRoutersConfig.getDataId());
            log.info("nacos.config.group = {}", gatewayRoutersConfig.getRouteGroup());
            String configInfo = configService.getConfig(gatewayRoutersConfig.getDataId(), gatewayRoutersConfig.getRouteGroup(), DEFAULT_TIMEOUT);
            if (StringUtils.isNotBlank(configInfo)) {
                log.info("获取网关当前配置:\r\n{}", configInfo);
                routes = JSON.parseArray(configInfo, RouteDefinition.class);
            }else{
                log.warn("ERROR: 从Nacos获取网关配置为空，请确认Nacos配置是否正确！");
            }
        } catch (NacosException e) {
            log.error("初始化网关路由时发生错误", e);
            e.printStackTrace();
        }
        for (RouteDefinition definition : routes) {
            log.info("update route : {}", definition.toString());
            dynamicRouteService.add(definition);
        }
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        dynamicRouteByNacosListener(gatewayRoutersConfig.getDataId(), gatewayRoutersConfig.getRouteGroup());
    }


    /**
     * 从redis中读取路由配置
     *
     * @return
     */
    private void loadRoutesByRedis(BaseMap baseMap) {
        List<MyRouteDefinition> routes = Lists.newArrayList();
        configService = createConfigService();
        if (configService == null) {
            log.warn("initConfigService fail");
        }
        Object configInfo = redisUtil.get(CacheConstant.GATEWAY_ROUTES);
        if (ObjectUtil.isNotEmpty(configInfo)) {
            log.info("获取网关当前配置:\r\n{}", configInfo);
            JSONArray array = JSON.parseArray(configInfo.toString());
            try {
                routes = getRoutesByJson(array);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }else{
            log.warn("ERROR: 从Redis获取网关配置为空，请确认system服务是否启动成功！");
        }
        
        for (MyRouteDefinition definition : routes) {
            log.info("update route : {}", definition.toString());
            Integer status=definition.getStatus();
            if(status.equals(0)){
                dynamicRouteService.delete(definition.getId());
            }else{
                dynamicRouteService.add(definition);
            }
        }
        if(ObjectUtils.isNotEmpty(baseMap)){
            String delRouterId = baseMap.get("delRouterId");
            if (ObjectUtils.isNotEmpty(delRouterId)) {
                dynamicRouteService.delete(delRouterId);
            }
        }
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    /**
     * redis中的信息需要处理下 转成RouteDefinition对象
     * - id: login
     * uri: lb://cloud-jeecg-system
     * predicates:
     * - Path=/jeecg-boot/sys/**,
     *
     * @param array
     * @return
     */

    public static List<MyRouteDefinition> getRoutesByJson(JSONArray array) throws URISyntaxException {
        List<MyRouteDefinition> ls = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            MyRouteDefinition route = new MyRouteDefinition();
            route.setId(obj.getString("routerId"));
            route.setStatus(obj.getInteger("status"));
            Object uri = obj.get("uri");
            if (uri == null) {
                route.setUri(new URI("lb://" + obj.getString("name")));
            } else {
                route.setUri(new URI(obj.getString("uri")));
            }
            Object predicates = obj.get("predicates");
            if (predicates != null) {
                //update-begin-author:liusq---date:2023-10-15--for: [issues/5331]网关路由配置问题
                List<PredicatesVo> list = JSON.parseArray(predicates.toString(), PredicatesVo.class);
                //获取合并后的Predicates，防止配置多个path导致路径失效的问题
                Map<String, List<String>> groupedPredicates = new HashMap<>();
                for (PredicatesVo predicatesVo : list) {
                    String name = predicatesVo.getName();
                    List<String> args = predicatesVo.getArgs();
                    groupedPredicates.computeIfAbsent(name, k -> new ArrayList<>()).addAll(args);
                }
                //合并后的list
                list = new ArrayList<>();
                for (Map.Entry<String, List<String>> entry : groupedPredicates.entrySet()) {
                    String name = entry.getKey();
                    List<String> args = entry.getValue();
                    list.add(new PredicatesVo(name, args));
                }
                //update-end-author:liusq---date:2023-10-15--for:[issues/5331]网关路由配置问题
                List<PredicateDefinition> predicateDefinitionList = new ArrayList<>();
                for (Object map : list) {
                    JSONObject json = JSON.parseObject(JSON.toJSONString(map));
                    PredicateDefinition predicateDefinition = new PredicateDefinition();
                    //update-begin-author:zyf date:20220419 for:【VUEN-762】路由条件添加异常问题,原因是部分路由条件参数需要设置固定key
                    String name=json.getString("name");
                    predicateDefinition.setName(name);
                    //路由条件是否拼接Key
                    if(ArrayUtil.contains(GEN_KEY_ROUTERS,name)) {
                        JSONArray jsonArray = json.getJSONArray("args");
                        for (int j = 0; j < jsonArray.size(); j++) {
                            predicateDefinition.addArg("_genkey" + j, jsonArray.get(j).toString());
                        }
                    }else{
                        JSONObject jsonObject = json.getJSONObject("args");
                        if(ObjectUtil.isNotEmpty(jsonObject)){
                            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                                Object valueObj=entry.getValue();
                                if(ObjectUtil.isNotEmpty(valueObj)) {
                                    predicateDefinition.addArg(entry.getKey(), valueObj.toString());
                                }
                            }
                        }
                    }
                    //update-end-author:zyf date:20220419 for:【VUEN-762】路由条件添加异常问题,原因是部分路由条件参数需要设置固定key
                    predicateDefinitionList.add(predicateDefinition);
                }
                route.setPredicates(predicateDefinitionList);
            }

            Object filters = obj.get("filters");
            if (filters != null) {
                JSONArray list = JSON.parseArray(filters.toString());
                List<FilterDefinition> filterDefinitionList = new ArrayList<>();
                if (ObjectUtil.isNotEmpty(list)) {
                    for (Object map : list) {
                        JSONObject json = (JSONObject) map;
                        JSONArray jsonArray = json.getJSONArray("args");
                        String name = json.getString("name");
                        FilterDefinition filterDefinition = new FilterDefinition();
                        for (Object o : jsonArray) {
                            JSONObject params = (JSONObject) o;
                            filterDefinition.addArg(params.getString("key"), params.get("value").toString());
                        }
                        filterDefinition.setName(name);
                        filterDefinitionList.add(filterDefinition);
                    }
                    route.setFilters(filterDefinitionList);
                }
            }
            ls.add(route);
        }
        return ls;
    }


//    private void loadRoutesByDataBase() {
//        List<GatewayRouteVo> routeList = jdbcTemplate.query(SELECT_ROUTES, new RowMapper<GatewayRouteVo>() {
//            @Override
//            public GatewayRouteVo mapRow(ResultSet rs, int i) throws SQLException {
//                GatewayRouteVo result = new GatewayRouteVo();
//                result.setId(rs.getString("id"));
//                result.setName(rs.getString("name"));
//                result.setUri(rs.getString("uri"));
//                result.setStatus(rs.getInt("status"));
//                result.setRetryable(rs.getInt("retryable"));
//                result.setPredicates(rs.getString("predicates"));
//                result.setStripPrefix(rs.getInt("strip_prefix"));
//                result.setPersist(rs.getInt("persist"));
//                return result;
//            }
//        });
//        if (ObjectUtil.isNotEmpty(routeList)) {
//            // 加载路由
//            routeList.forEach(route -> {
//                RouteDefinition definition = new RouteDefinition();
//                List<PredicateDefinition> predicatesList = Lists.newArrayList();
//                List<FilterDefinition> filtersList = Lists.newArrayList();
//                definition.setId(route.getId());
//                String predicates = route.getPredicates();
//                String filters = route.getFilters();
//                if (StringUtils.isNotEmpty(predicates)) {
//                    predicatesList = JSON.parseArray(predicates, PredicateDefinition.class);
//                    definition.setPredicates(predicatesList);
//                }
//                if (StringUtils.isNotEmpty(filters)) {
//                    filtersList = JSON.parseArray(filters, FilterDefinition.class);
//                    definition.setFilters(filtersList);
//                }
//                URI uri = UriComponentsBuilder.fromUriString(route.getUri()).build().toUri();
//                definition.setUri(uri);
//                this.repository.save(Mono.just(definition)).subscribe();
//            });
//            log.info("加载路由:{}==============", routeList.size());
//            Mono.empty();
//        }
//    }


    /**
     * 监听Nacos下发的动态路由配置
     *
     * @param dataId
     * @param group
     */
    public void dynamicRouteByNacosListener(String dataId, String group) {
        try {
            configService.addListener(dataId, group, new Listener() {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    log.info("进行网关更新:\n\r{}", configInfo);
                    List<MyRouteDefinition> definitionList = JSON.parseArray(configInfo, MyRouteDefinition.class);
                    for (MyRouteDefinition definition : definitionList) {
                        log.info("update route : {}", definition.toString());
                        dynamicRouteService.update(definition);
                    }
                }

                @Override
                public Executor getExecutor() {
                    log.info("getExecutor\n\r");
                    return null;
                }
            });
        } catch (Exception e) {
            log.error("从nacos接收动态路由配置出错!!!", e);
        }
    }

    /**
     * 创建ConfigService
     *
     * @return
     */
    private ConfigService createConfigService() {
        try {
            Properties properties = new Properties();
            properties.setProperty("serverAddr", gatewayRoutersConfig.getServerAddr());
            if(StringUtils.isNotBlank(gatewayRoutersConfig.getNamespace())){
                properties.setProperty("namespace", gatewayRoutersConfig.getNamespace());
            }
            if(StringUtils.isNotBlank( gatewayRoutersConfig.getUsername())){
                properties.setProperty("username", gatewayRoutersConfig.getUsername());
            }
            if(StringUtils.isNotBlank(gatewayRoutersConfig.getPassword())){
                properties.setProperty("password", gatewayRoutersConfig.getPassword());
            }
            return configService = NacosFactory.createConfigService(properties);
        } catch (Exception e) {
            log.error("创建ConfigService异常", e);
            return null;
        }
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }
}
