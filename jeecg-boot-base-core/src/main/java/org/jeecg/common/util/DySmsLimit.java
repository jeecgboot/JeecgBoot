package org.jeecg.common.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 防止刷短信接口（只针对绑定手机号模板：SMS_175430166）
 * 
 * 1、同一IP，1分钟内发短信不允许超过5次（每一分钟重置每个IP请求次数）
 * 2、同一IP，1分钟内发短信超过20次，进入黑名单，不让使用短信接口
 * 
 * 3、短信接口加签和时间戳
 *  涉及接口：
 *  /sys/sms
 *  /desform/api/sendVerifyCode
 *  /sys/sendChangePwdSms
 */
@Slf4j
public class DySmsLimit {

    // 1分钟内最大发短信数量（单一IP）
    private static final int MAX_MESSAGE_PER_MINUTE = 5;
    // 1分钟
    private static final int MILLIS_PER_MINUTE = 60000;
    // 一分钟内报警线最大短信数量，超了进黑名单（单一IP）
    private static final int MAX_TOTAL_MESSAGE_PER_MINUTE = 20;

    private static ConcurrentHashMap<String, Long> ipLastRequestTime = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Integer> ipRequestCount = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Boolean> ipBlacklist = new ConcurrentHashMap<>();

    /**
     * @param ip 请求发短信的IP地址
     * @return
     */
    public static boolean canSendSms(String ip) {
        long currentTime = System.currentTimeMillis();
        long lastRequestTime = ipLastRequestTime.getOrDefault(ip, 0L);
        int requestCount = ipRequestCount.getOrDefault(ip, 0);
        log.info("IP：{}, Msg requestCount：{} ", ip, requestCount);

        if (ipBlacklist.getOrDefault(ip, false)) {
            // 如果IP在黑名单中，则禁止发送短信
            log.error("IP：{}, 进入黑名单，禁止发送请求短信！", ip);
            return false;
        }

        if (currentTime - lastRequestTime >= MILLIS_PER_MINUTE) {
            // 如果距离上次请求已经超过一分钟，则重置计数
            ipRequestCount.put(ip, 1);
            ipLastRequestTime.put(ip, currentTime);
            return true;
        } else {
            // 如果距离上次请求不到一分钟
            ipRequestCount.put(ip, requestCount + 1);
            if (requestCount < MAX_MESSAGE_PER_MINUTE) {
                // 如果请求次数小于5次，允许发送短信
                return true;
            } else if (requestCount >= MAX_TOTAL_MESSAGE_PER_MINUTE) {
                // 如果请求次数超过报警线短信数量，将IP加入黑名单
                ipBlacklist.put(ip, true);
                return false;
            } else {
                log.error("IP：{}, 1分钟内请求短信超过5次，请稍后重试！", ip);
                return false;
            }
        }
    }

    /**
     * 图片二维码验证成功之后清空数量
     * 
     * @param ip IP地址
     */
    public static void clearSendSmsCount(String ip) {
        long currentTime = System.currentTimeMillis();
        ipRequestCount.put(ip, 0);
        ipLastRequestTime.put(ip, currentTime);
    }
    
//    public static void main(String[] args) {
//        String ip = "192.168.1.1";
//        for (int i = 1; i < 50; i++) {
//            if (canSendSms(ip)) {
//                System.out.println("Send SMS successfully");
//            } else {
//                //System.out.println("Exceed SMS limit for IP " + ip);
//            }
//        }
//
//        System.out.println(ipLastRequestTime);
//        System.out.println(ipRequestCount);
//        System.out.println(ipBlacklist);
//    }
}
