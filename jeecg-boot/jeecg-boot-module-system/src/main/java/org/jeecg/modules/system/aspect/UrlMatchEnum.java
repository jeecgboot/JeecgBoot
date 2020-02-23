package org.jeecg.modules.system.aspect;

/**
 * @Author scott
 * @Date 2020/1/14 13:36
 * @Description: 请求URL与菜单路由URL转换规则（方便于采用菜单路由URL来配置数据权限规则）
 */
public enum UrlMatchEnum {
    CGFORM_DATA("/online/cgform/api/getData/", "/online/cgformList/"),
    CGFORM_TREE_DATA("/online/cgform/api/getTreeData/", "/online/cgformList/");

    UrlMatchEnum(String url, String match_url) {
        this.url = url;
        this.match_url = match_url;
    }

    /**
     * Request 请求 URL前缀
     */
    private String url;
    /**
     * 菜单路由 URL前缀 (对应菜单路径)
     */
    private String match_url;

    /**
     * 根据req url 获取到菜单配置路径（前端页面路由URL）
     *
     * @param url
     * @return
     */
    public static String getMatchResultByUrl(String url) {
        //获取到枚举
        UrlMatchEnum[] values = UrlMatchEnum.values();
        //加强for循环进行遍历操作
        for (UrlMatchEnum lr : values) {
            //如果遍历获取的type和参数type一致
            if (url.indexOf(lr.url) != -1) {
                //返回type对象的desc
                return url.replace(lr.url, lr.match_url);
            }
        }
        return null;
    }


    public static void main(String[] args) {
        /**
         * 比如request真实请求URL: /online/cgform/api/getData/81fcf7d8922d45069b0d5ba983612d3a
         * 转换匹配路由URL后（对应配置的菜单路径）:/online/cgformList/81fcf7d8922d45069b0d5ba983612d3a
         */
        System.out.println(UrlMatchEnum.getMatchResultByUrl("/online/cgform/api/getData/81fcf7d8922d45069b0d5ba983612d3a"));
    }
}