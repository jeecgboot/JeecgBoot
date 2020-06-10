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

    //文件类型
    public  static class fileType{
        public static String IMAGE = "image";

    }
    public  static class tables{
        /**
         *企业基本信息表
         */
        public static String BASEINFO = "company_baseinfo";
        /**
         * 竣工验收信息表
         */
        public static String ACCEPTANCE = "company_acceptance";
        /**
         * 污染防治信息表
         */
        public static String PREVENTION = "company_prevention";
        /**
         *排污许可证表
         */
        public static String DIRTYALLOW = "company_dirty_allow";
        /**
         * 环保税信息表
         */
        public static String ENVTAX = "company_env_tax";
        /**
         * 清洁生产表
         */
        public static String CLEANPRODUCT = "company_clean_product";
        /**'
         * 在线监控验收信息
         */
        public static String ONLINEINFO = "company_online_info";
        /**
         * 资质表
         */
        public static String QUALIFICATION = "company_qualification";


    }

}
