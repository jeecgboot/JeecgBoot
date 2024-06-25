package org.jeecg.common.desensitization.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jeecg.common.desensitization.annotation.SensitiveDecode;
import org.jeecg.common.desensitization.annotation.SensitiveEncode;
import org.jeecg.common.desensitization.util.SensitiveInfoUtil;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 敏感数据切面处理类
 * @Author taoYan
 * @Date 2022/4/20 17:45
 **/
@Slf4j
@Aspect
@Component
public class SensitiveDataAspect {

    /**
     * 定义切点Pointcut
     */
    @Pointcut("@annotation(org.jeecg.common.desensitization.annotation.SensitiveEncode) || @annotation(org.jeecg.common.desensitization.annotation.SensitiveDecode)")
    public void sensitivePointCut() {
    }

    @Around("sensitivePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 处理结果
        Object result = point.proceed();
        if(result == null){
            return result;
        }
        Class resultClass = result.getClass();
        log.debug(" resultClass  = {}" , resultClass);

        if(resultClass.isPrimitive()){
            //是基本类型 直接返回 不需要处理
            return result;
        }
        // 获取方法注解信息：是哪个实体、是加密还是解密
        boolean isEncode = true;
        Class entity = null;
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        SensitiveEncode encode = method.getAnnotation(SensitiveEncode.class);
        if(encode==null){
            SensitiveDecode decode = method.getAnnotation(SensitiveDecode.class);
            if(decode!=null){
                entity = decode.entity();
                isEncode = false;
            }
        }else{
            entity = encode.entity();
        }

        long startTime=System.currentTimeMillis();
        if(resultClass.equals(entity) || entity.equals(Object.class)){
            // 方法返回实体和注解的entity一样，如果注解没有申明entity属性则认为是(方法返回实体和注解的entity一样)
            SensitiveInfoUtil.handlerObject(result, isEncode);
        } else if(result instanceof List){
            // 方法返回List<实体>
            SensitiveInfoUtil.handleList(result, entity, isEncode);
        }else{
            // 方法返回一个对象
            SensitiveInfoUtil.handleNestedObject(result, entity, isEncode);
        }
        long endTime=System.currentTimeMillis();
        log.info((isEncode ? "加密操作，" : "解密操作，") + "Aspect程序耗时：" + (endTime - startTime) + "ms");

        return result;
    }

}
