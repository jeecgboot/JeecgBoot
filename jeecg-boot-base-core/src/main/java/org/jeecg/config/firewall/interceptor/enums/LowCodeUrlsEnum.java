package org.jeecg.config.firewall.interceptor.enums;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * @author: qinfeng
 * @date: 2023/09/04 11:44
 */
public enum LowCodeUrlsEnum {
    /**
     * online表单配置请求 TODO 增改删
     */
    NEW_LOW_APP_ADD_URL("/online/cgform/api/addAll", "添加online表单"),
    NEW_LOW_APP_EDIT_URL("/online/cgform/api/editAll", "编辑online表单"),
    ONLINE_DB_SYNC("/online/cgform/api/doDbSynch/**/**", "online表单同步数据库"),
    ONLINE_DEL_BATCH("/online/cgform/head/deleteBatch", "online表单批量删除"),
    ONLINE_DELETE("/online/cgform/head/delete", "online表单删除"),
    ONLINE_REMOVE("/online/cgform/head/removeRecord", "online表单移除"),
    ONLINE_COPY("/online/cgform/head/copyOnline", "online表单生成视图"),
    ONLINE_TABLE("/online/cgform/head/copyOnlineTable", "online表单复制表"),
    ONLINE_BUTTON_AI_TEST("/online/cgform/button/aitest", "online表单自定义按钮生成数据"),
    ONLINE_BUTTON_ADD("/online/cgform/button/add", "online表单自定义按钮新增"),
    ONLINE_BUTTON_EDIT("/online/cgform/button/edit", "online表单自定义按钮编辑"),
    ONLINE_BUTTON_DEL("/online/cgform/button/deleteBatch", "online表单自定义按钮删除"),
    ONLINE_ENHANCE_JS("/online/cgform/head/enhanceJs/**", "online表单JS增强"),
    ONLINE_ENHANCE_JAVA("/online/cgform/head/enhanceJava/**", "online表单JAVA增强"),
    /**
     * online报表配置请求
     */
    ONLINE_CG_REPORT_ADD("/online/cgreport/head/add", "online报表新增"),
    ONLINE_CG_REPORT_EDIT("/online/cgreport/head/editAll", "online报表编辑"),
    ONLINE_CG_REPORT_DEL("/online/cgreport/head/delete", "online报表删除"),
    ONLINE_CG_REPORT_PARSE_SQL("/online/cgreport/head/parseSql", "online报表SQL解析"),
    /**
     * online图表配置请求
     */
    ONLINE_GRAPH_REPORT_ADD("/online/graphreport/head/add", "online图表新增"),
    ONLINE_GRAPH_REPORT_EDIT("/online/graphreport/head/edit", "online图表编辑"),
    ONLINE_GRAPH_REPORT_DEL("/online/graphreport/head/deleteBatch", "online图表删除"),
    ONLINE_GRAPH_REPORT_PARSE_SQL("/online/cgreport/head/parseSql", "online图表解析SQL"),

    /**
     * 大屏配置请求
     */
    BIG_SCREEN_DB_ADD("/bigscreen/bigScreenDb/add", "大屏数据源新增"),
    BIG_SCREEN_DB_EDIT("/bigscreen/bigScreenDb/edit", "大屏数据源编辑"),
    BIG_SCREEN_DB_DEL("/bigscreen/bigScreenDb/delete", "大屏数据源删除"),
    BIG_SCREEN_DB_TEST_CONNECTION("/bigscreen/bigScreenDb/testConnection", "大屏数据源连接测试"),
//    BIG_SCREEN_SAVE("/bigscreen/visual/save", "大屏新增"),
//    BIG_SCREEN_EDIT("/bigscreen/visual/update", "大屏编辑"),
//    BIG_SCREEN_COPY("/bigscreen/visual/copy", "大屏复制"),
//    BIG_SCREEN_REMOVE("/bigscreen/visual/remove", "大屏移除"),
//    BIG_SCREEN_DEL("/bigscreen/visual/deleteById", "大屏删除"),

    /**
     * 仪表盘配置请求
     */
    DRAG_DB_ADD("/drag/onlDragDataSource/add", "仪表盘数据源新增"),
    DRAG_DB_TEST_CONNECTION("/drag/onlDragDataSource/testConnection", "仪表盘数据源连接测试"),
    DRAG_PARSE_SQL("/drag/onlDragDatasetHead/queryFieldBySql", "仪表盘数据集SQL解析"),
    DRAG_DATASET_ADD("/drag/onlDragDatasetHead/add", "仪表盘数据集新增");

    /**
     * 其他配置请求
     */

    private String url;
    private String title;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    LowCodeUrlsEnum(String url, String title) {
        this.url = url;
        this.title = title;
    }

    /**
     * 根据code获取可用的数量
     *
     * @return
     */
    public static List<String> getLowCodeInterceptUrls() {
        return Arrays.stream(LowCodeUrlsEnum.values()).map(LowCodeUrlsEnum::getUrl).collect(Collectors.toList());
    }

}
