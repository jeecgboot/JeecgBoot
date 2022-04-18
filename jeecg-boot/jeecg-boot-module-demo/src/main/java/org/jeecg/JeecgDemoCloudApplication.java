//package org.jeecg;
//
//import org.jeecg.common.base.BaseMap;
//import org.jeecg.common.constant.GlobalConstants;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.openfeign.EnableFeignClients;
//import org.springframework.data.redis.core.RedisTemplate;
//
//@SpringBootApplication
//@EnableFeignClients
//public class JeecgDemoCloudApplication implements CommandLineRunner {
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
//
//    public static void main(String[] args) {
//        SpringApplication.run(JeecgDemoCloudApplication.class, args);
//    }
//
//    /**
//     * 启动的时候，触发下gateway网关刷新
//     *
//     * 解决： 先启动gateway后启动服务，Swagger接口文档访问不通的问题
//     * @param args
//     */
//    @Override
//    public void run(String... args) {
//        BaseMap params = new BaseMap();
//        params.put(GlobalConstants.HANDLER_NAME, GlobalConstants.LODER_ROUDER_HANDLER);
//        //刷新网关
//        redisTemplate.convertAndSend(GlobalConstants.REDIS_TOPIC_NAME, params);
//    }
//}
