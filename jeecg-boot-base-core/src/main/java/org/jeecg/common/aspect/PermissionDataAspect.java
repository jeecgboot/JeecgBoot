package org.jeecg.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.system.query.QueryRuleEnum;
import org.jeecg.common.system.util.JeecgDataAutorUtils;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.SysPermissionDataRuleModel;
import org.jeecg.common.system.vo.SysUserCacheInfo;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 数据权限切面处理类
 *  当被请求的方法有注解PermissionData时,会在往当前request中写入数据权限信息
 * @Date 2019年4月10日
 * @Version: 1.0
 * @author: jeecg-boot
 */
@Aspect
@Component
@Slf4j
public class PermissionDataAspect {
    @Lazy
    @Autowired
    private CommonAPI commonApi;

    private static final String SPOT_DO = ".do";

    @Pointcut("@annotation(org.jeecg.common.aspect.annotation.PermissionData)")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object arround(ProceedingJoinPoint point) throws  Throwable{
        HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        PermissionData pd = method.getAnnotation(PermissionData.class);
        String component = pd.pageComponent();
        String requestMethod = request.getMethod();
        String requestPath = request.getRequestURI().substring(request.getContextPath().length());
        requestPath = filterUrl(requestPath);
        //update-begin-author:taoyan date:20211027 for:JTC-132【online报表权限】online报表带参数的菜单配置数据权限无效
        //先判断是否online报表请求
        if(requestPath.indexOf(UrlMatchEnum.CGREPORT_DATA.getMatchUrl())>=0 || requestPath.indexOf(UrlMatchEnum.CGREPORT_ONLY_DATA.getMatchUrl())>=0){
            // 获取地址栏参数
            String urlParamString = request.getParameter(CommonConstant.ONL_REP_URL_PARAM_STR);
            if(oConvertUtils.isNotEmpty(urlParamString)){
                requestPath+="?"+urlParamString;
            }
        }
        //update-end-author:taoyan date:20211027 for:JTC-132【online报表权限】online报表带参数的菜单配置数据权限无效
        log.debug("拦截请求 >> {} ; 请求类型 >> {} . ", requestPath, requestMethod);
        String username = JwtUtil.getUserNameByToken(request);
        //查询数据权限信息
        //TODO 微服务情况下也得支持缓存机制
        List<SysPermissionDataRuleModel> dataRules = commonApi.queryPermissionDataRule(component, requestPath, username);
        if(dataRules!=null && dataRules.size()>0) {
            //临时存储
            JeecgDataAutorUtils.installDataSearchConditon(request, dataRules);
            //TODO 微服务情况下也得支持缓存机制
            SysUserCacheInfo userinfo = commonApi.getCacheUser(username);
            JeecgDataAutorUtils.installUserInfo(request, userinfo);
        }
        return  point.proceed();
    }

    private String filterUrl(String requestPath){
        String url = "";
        if(oConvertUtils.isNotEmpty(requestPath)){
            url = requestPath.replace("\\", "/");
            url = url.replace("//", "/");
            if(url.indexOf(SymbolConstant.DOUBLE_SLASH)>=0){
                url = filterUrl(url);
            }
			/*if(url.startsWith("/")){
				url=url.substring(1);
			}*/
        }
        return url;
    }

    /**
     * 获取请求地址
     * @param request
     * @return
     */
    @Deprecated
    private String getJgAuthRequsetPath(HttpServletRequest request) {
        String queryString = request.getQueryString();
        String requestPath = request.getRequestURI();
        if(oConvertUtils.isNotEmpty(queryString)){
            requestPath += "?" + queryString;
        }
        // 去掉其他参数(保留一个参数) 例如：loginController.do?login
        if (requestPath.indexOf(SymbolConstant.AND) > -1) {
            requestPath = requestPath.substring(0, requestPath.indexOf("&"));
        }
        if(requestPath.indexOf(QueryRuleEnum.EQ.getValue())!=-1){
            if(requestPath.indexOf(SPOT_DO)!=-1){
                requestPath = requestPath.substring(0,requestPath.indexOf(".do")+3);
            }else{
                requestPath = requestPath.substring(0,requestPath.indexOf("?"));
            }
        }
        // 去掉项目路径
        requestPath = requestPath.substring(request.getContextPath().length() + 1);
        return filterUrl(requestPath);
    }

    @Deprecated
    private boolean moHuContain(List<String> list,String key){
        for(String str : list){
            if(key.contains(str)){
                return true;
            }
        }
        return false;
    }


}
