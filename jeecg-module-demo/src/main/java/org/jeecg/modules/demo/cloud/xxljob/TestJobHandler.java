//
//package org.jeecg.modules.demo.cloud.xxljob;
//
//import com.xxl.job.core.biz.model.ReturnT;
//import com.xxl.job.core.handler.annotation.XxlJob;
//import lombok.extern.slf4j.Slf4j;
//import org.jeecg.common.config.mqtoken.UserTokenContext;
//import org.jeecg.common.constant.CommonConstant;
//import org.jeecg.common.system.api.ISysBaseAPI;
//import org.jeecg.common.system.util.JwtUtil;
//import org.jeecg.common.util.RedisUtil;
//import org.jeecg.common.util.SpringContextUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//
///**
// * xxl-job定时任务测试
// */
//@Slf4j
//@Component
//public class TestJobHandler {
//    @Autowired
//    ISysBaseAPI sysBaseApi;
//
//    /**
//     * 简单任务
//     *
//     * 测试：无token调用feign接口
//     *
//     * @param params
//     * @return
//     */
//
//    @XxlJob(value = "testJob")
//    public ReturnT<String> demoJobHandler(String params) {
//        //1.生成临时令牌Token到线程中
//        UserTokenContext.setToken(getTemporaryToken());
//
//        log.info("我是 jeecg-demo 服务里的定时任务 testJob , 我执行了...............................");
//        log.info("我调用 jeecg-system 服务的字典接口：{}",sysBaseApi.queryAllDict());
//        //。。。此处可以写多个feign接口调用
//
//        //2.使用完，删除临时令牌Token
//        UserTokenContext.remove();
//        return ReturnT.SUCCESS;
//    }
//
//    public void init() {
//        log.info("init");
//    }
//
//    public void destroy() {
//        log.info("destory");
//    }
//
//    /**
//     * 获取临时令牌
//     *
//     * 模拟登陆接口，获取模拟 Token
//     * @return
//     */
//    public static String getTemporaryToken() {
//        RedisUtil redisUtil = SpringContextUtils.getBean(RedisUtil.class);
//        // 模拟登录生成Token
//        String token = JwtUtil.sign("??", "??");
//        // 设置Token缓存有效时间为 5 分钟
//        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
//        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, 5 * 60 * 1000);
//        return token;
//    }
//
//}
//
