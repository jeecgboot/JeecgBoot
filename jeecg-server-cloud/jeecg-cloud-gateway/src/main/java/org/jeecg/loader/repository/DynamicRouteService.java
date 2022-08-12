package org.jeecg.loader.repository;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.loader.repository.MyInMemoryRouteDefinitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * 动态更新路由网关service
 * 1）实现一个Spring提供的事件推送接口ApplicationEventPublisherAware
 * 2）提供动态路由的基础方法，可通过获取bean操作该类的方法。该类提供新增路由、更新路由、删除路由，然后实现发布的功能。
 *
 * @author zyf
 */
@Slf4j
@Service
public class DynamicRouteService implements ApplicationEventPublisherAware {

    @Autowired
    private MyInMemoryRouteDefinitionRepository repository;

    /**
     * 发布事件
     */

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    /**
     * 删除路由
     *
     * @param id
     * @return
     */
    public synchronized void delete(String id) {
        try {
            repository.delete(Mono.just(id)).subscribe();
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
        }catch (Exception e){
            log.warn(e.getMessage(),e);
        }
    }

    /**
     * 更新路由
     *
     * @param definition
     * @return
     */
    public synchronized String update(RouteDefinition definition) {
        try {
            log.info("gateway update route {}", definition);
        } catch (Exception e) {
            return "update fail,not find route  routeId: " + definition.getId();
        }
        try {
            repository.save(Mono.just(definition)).subscribe();
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
            return "success";
        } catch (Exception e) {
            return "update route fail";
        }
    }

    /**
     * 增加路由
     *
     * @param definition
     * @return
     */
    public synchronized String add(RouteDefinition definition) {
        log.info("gateway add route {}", definition);
        try {
            repository.save(Mono.just(definition)).subscribe();
        } catch (Exception e) {
            log.warn(e.getMessage(),e);
        }
        return "success";
    }
}