package org.jeecg.config;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public final class LogUtil {

    //记录方法耗时时间
    private static final Map<String, Long> recordMap = new HashMap<>(200);

    public static void startTime(String name) {
        recordMap.put(name, System.currentTimeMillis());
        log.info("{} 方法开始执行", name);
    }

    public static void endTime(String name, String msg) {
        Long start = recordMap.get(name);
        if (start != null) {
            log.info("{} -- {} {} 方法调用结束，耗时{}秒", DateUtils.now(), name, msg, String.format("%.2f", (System.currentTimeMillis() - start) / 1000D));
        } else {
            log.info("{} -- {} {} 方法调用结束", DateUtils.now(), name, msg);
        }
    }

    public static void endTime(String name) {
        endTime(name, "");
    }
}
