package org.jeecg.config.vo;

/**
 * 平台安全配置
 *
 * @author: scott
 * @date: 2023年09月05日 9:25
 */
public class Firewall {
    /**
     * 数据源安全 (开启后，Online报表和图表的数据源为必填)
     */
    private Boolean dataSourceSafe = false;
    /**
     * 低代码模式（dev:开发模式，prod:发布模式——关闭所有在线开发配置能力）
     */
    private String lowCodeMode;
//    /**
//     * 表字典安全模式（white:白名单——配置了白名单的表才能通过表字典方式访问，black:黑名单——配置了黑名单的表不允许表字典方式访问）
//     */
//    private String tableDictMode;

    public Boolean getDataSourceSafe() {
        return dataSourceSafe;
    }

    public void setDataSourceSafe(Boolean dataSourceSafe) {
        this.dataSourceSafe = dataSourceSafe;
    }

    public String getLowCodeMode() {
        return lowCodeMode;
    }

    public void setLowCodeMode(String lowCodeMode) {
        this.lowCodeMode = lowCodeMode;
    }

}
