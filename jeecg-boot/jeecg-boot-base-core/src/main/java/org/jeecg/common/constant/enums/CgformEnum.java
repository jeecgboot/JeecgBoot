package org.jeecg.common.constant.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * online表单枚举 代码生成器用到
 * @author: jeecg-boot
 */
public enum CgformEnum {

    /**
     * 单表
     */
    ONE(1, "one", "/jeecg/code-template-online", "default.one", "经典风格", new String[]{"vue3","vue","vue3Native"}),

    /**
     * 多表
     */
    MANY(2, "many", "/jeecg/code-template-online", "default.onetomany", "经典风格" ,new String[]{"vue"}),
    /**
     * 多表（jvxe风格）
     *  */
    JVXE_TABLE(2, "jvxe", "/jeecg/code-template-online", "jvxe.onetomany", "默认风格" ,new String[]{"vue3","vue","vue3Native"}),

    /**
     * 多表 (erp风格)
     */
    ERP(2, "erp", "/jeecg/code-template-online", "erp.onetomany", "ERP风格" ,new String[]{"vue3","vue","vue3Native"}),
    /**
     * 多表（内嵌子表风格）
     */
    INNER_TABLE(2, "innerTable", "/jeecg/code-template-online", "inner-table.onetomany", "内嵌子表风格" ,new String[]{"vue3","vue"}),
    /**
     * 多表（tab风格）
     *  */
    TAB(2, "tab", "/jeecg/code-template-online", "tab.onetomany", "Tab风格" ,new String[]{"vue3","vue"}),
    /**
     * 树形列表
     */
    TREE(3, "tree", "/jeecg/code-template-online", "default.tree", "树形列表" ,new String[]{"vue3","vue","vue3Native"});

    /**
     * 类型 1/单表 2/一对多 3/树
     */
    int type;
    /**
     * 编码标识
     */
    String code;
    /**
     * 代码生成器模板路径
     */
    String templatePath;
    /**
     * 代码生成器模板路径
     */
    String stylePath;
    /**
     * 模板风格名称
     */
    String note;
    /**
     * 支持代码风格 vue3:vue3包装代码 vue3Native:vue3原生代码 vue:vue2代码
     */
    String[] vueStyle;

    /**
     * 构造器
     *
     * @param type 类型 1/单表 2/一对多 3/树
     * @param code 模板编码
     * @param templatePath  模板路径
     * @param stylePath  模板子路径
     * @param note
     * @param vueStyle 支持代码风格
     */
    CgformEnum(int type, String code, String templatePath, String stylePath, String note, String[] vueStyle) {
        this.type = type;
        this.code = code;
        this.templatePath = templatePath;
        this.stylePath = stylePath;
        this.note = note;
        this.vueStyle = vueStyle;
    }

    /**
     * 根据code获取模板路径
     *
     * @param code
     * @return
     */
    public static String getTemplatePathByConfig(String code) {
        return getCgformEnumByConfig(code).templatePath;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getStylePath() {
        return stylePath;
    }

    public void setStylePath(String stylePath) {
        this.stylePath = stylePath;
    }

    public String[] getVueStyle() {
        return vueStyle;
    }

    public void setVueStyle(String[] vueStyle) {
        this.vueStyle = vueStyle;
    }

    /**
     * 根据code找枚举
     *
     * @param code
     * @return
     */
    public static CgformEnum getCgformEnumByConfig(String code) {
        for (CgformEnum e : CgformEnum.values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }

    /**
     * 根据类型找所有
     *
     * @param type
     * @return
     */
    public static List<Map<String, Object>> getJspModelList(int type) {
        List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();
        for (CgformEnum e : CgformEnum.values()) {
            if (e.type == type) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("code", e.code);
                map.put("note", e.note);
                ls.add(map);
            }
        }
        return ls;
    }


}
