package org.jeecg.modules.business.vo;

import lombok.Getter;

@Getter
public enum LogisticCompanyEnum {
        DISIFANG ("递四方"),
        CNE ("CNE"),
        CHUKOUYI ("出口易"),
        ANTU ("安途"),
        MIAOXIN ("淼信"),
        YUNTU ("云途"),
        JITU ("极兔"),
        WANGUOYOULIAN ("万国优联"),
        WANGYISUDA ("网易速达"),
        YIDA ("义达"),
        UBI ("UBI"),
        JIEHANG ("杰航"),
        WANBANG ("万邦"),
        WIA ("WIA"),
        CHENMINGKUNXIAOBAO ("晨明坤小包"),
        CAINIAO ("菜鸟"),
        SHENZHENYUANPENG ("深圳远朋"),
        WEIKELU("维客路（海外仓）"),
        WANTONGWULIU ("万通物流");

        private final String name;

        LogisticCompanyEnum(String name) {
            this.name = name;
        }
}
