package org.jeecg.modules.aop;

import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jeecg.common.api.dto.LogDTO;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.system.entity.SysTenantPack;
import org.jeecg.modules.system.entity.SysTenantPackUser;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @Author taoYan
 * @Date 2023/2/16 14:27
 **/
@Aspect
@Component
public class TenantPackUserLogAspect {

    @Resource
    private BaseCommonService baseCommonService;

    @Pointcut("@annotation(org.jeecg.modules.aop.TenantLog)")
    public void tenantLogPointCut() {

    }

    @Around("tenantLogPointCut()")
    public Object aroundMethod(ProceedingJoinPoint joinPoint)throws Throwable {
        //System.out.println("环绕通知>>>>>>>>>");

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        TenantLog log = method.getAnnotation(TenantLog.class);
        if(log != null){
            int opType = log.value();
            Integer logType = null;
            String content = null;
            Integer tenantId = null;
            //获取参数
            Object[] args = joinPoint.getArgs();
            if(args.length>0){
                for(Object obj: args){
                    if(obj instanceof SysTenantPack){
                        // logType=3 租户操作日志
                        logType = CommonConstant.LOG_TYPE_3;
                        SysTenantPack pack = (SysTenantPack)obj;
                        if(opType==2){
                            content = "创建了角色权限 "+ pack.getPackName();
                        }
                        tenantId = pack.getTenantId();
                        break;
                    }else if(obj instanceof SysTenantPackUser){
                        logType = CommonConstant.LOG_TYPE_3;
                        SysTenantPackUser packUser = (SysTenantPackUser)obj;
                        if(opType==2){
                            content = "将 "+packUser.getRealname()+" 添加到角色 "+ packUser.getPackName();
                        }else if(opType==4){
                            content = "移除了 "+packUser.getPackName()+" 成员 "+ packUser.getRealname();
                        }
                        tenantId = packUser.getTenantId();
                    }
                } 
            }
            if(logType!=null){
                LogDTO dto = new LogDTO();
                dto.setLogType(logType);
                dto.setLogContent(content);
                dto.setOperateType(opType);
                dto.setTenantId(tenantId);
                //获取登录用户信息
                LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                if(sysUser!=null){
                    dto.setUserid(sysUser.getUsername());
                    dto.setUsername(sysUser.getRealname());

                }
                dto.setCreateTime(new Date());
                //保存系统日志
                baseCommonService.addLog(dto);
            }
        }
        return joinPoint.proceed();
    }

    @AfterThrowing("tenantLogPointCut()")
    public void afterThrowing()throws Throwable{
        System.out.println("异常通知");
    }
}
