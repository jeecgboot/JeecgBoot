package org.jeecg.modules.im.base.util;

import java.util.UUID;

/**
 * Created by junko on 2017/4/25.
 */
public class UUIDTool {
    /**
     * 自动生成32位的UUid，对应数据库的主键id进行插入用。
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    public static String getUUID2() {
        return UUID.randomUUID().toString();
    }
}
