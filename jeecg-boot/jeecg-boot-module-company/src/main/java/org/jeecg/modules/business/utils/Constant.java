package org.jeecg.modules.business.utils;

import java.util.jar.JarEntry;

public class Constant {
    //状态
    public  static class status{
        /**
         * 暂存
         */
        public static String TEMPORARY = "0";
        /**
         * 待审核 刚提交的状态
         */
        public static String PEND = "1";
        /**
         * 正常 正在生效装填
         */
        public static String NORMAL = "2";
        /**
        *审核不通过
        */
        public static  String NOPASS = "3";
        /**
         * 过期  无效的状态--历史数据
         */
        public static String EXPIRED = "4";

    }
}
