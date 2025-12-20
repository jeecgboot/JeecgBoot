package org.jeecg.monitor.aspect;

import org.jeecg.monitor.annotation.BizMetric;
import org.jeecg.monitor.entity.BizMonitorMetric;
import org.jeecg.monitor.service.IBizMonitorMetricService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jeecg.common.util.SpringContextUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 业务指标采集切面
 *
 * @author JeecgBoot
 * @version 1.0
 */
@Aspect
@Component
public class BizMetricAspect {

    @Pointcut("@annotation(org.jeecg.monitor.annotation.BizMetric)")
    public void metricPointCut() {
    }

    @Around("metricPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object result = point.proceed();
        long time = System.currentTimeMillis() - beginTime;

        // 保存业务指标
        saveBizMetric(point, time, result);

        return result;
    }

    private void saveBizMetric(ProceedingJoinPoint joinPoint, long time, Object obj) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        BizMetric bizMetric = method.getAnnotation(BizMetric.class);
        if (bizMetric == null) {
            return;
        }

        // 获取业务指标服务
        IBizMonitorMetricService metricService = SpringContextUtils.getBean(IBizMonitorMetricService.class);

        // 创建业务指标快照
        BizMonitorMetric metric = new BizMonitorMetric();
        metric.setMetricCode(bizMetric.metricCode());
        metric.setMetricName(bizMetric.metricName());
        metric.setMetricValue(1.0); // 默认值为1，实际应根据业务逻辑调整
        metric.setBizTags(bizMetric.bizType());
        metric.setCollectTime(new Date());
        metric.setCreateTime(new Date());

        // 保存指标
        try {
            metricService.save(metric);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
