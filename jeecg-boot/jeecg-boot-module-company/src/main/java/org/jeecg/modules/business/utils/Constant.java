package org.jeecg.modules.business.utils;

public class Constant {
    //状态
    public  static class status{
        /**
         * 正常 正在生效装填
         */
        public static String NORMAL = "NORMAL";
        /**
         * 过期  无效的状态--历史数据
         */
        public static String EXPIRED = "EXPIRED";
        /**
         * 待审核 刚提交的状态
         */
        public static String PEND = "PEND";
    }
}
