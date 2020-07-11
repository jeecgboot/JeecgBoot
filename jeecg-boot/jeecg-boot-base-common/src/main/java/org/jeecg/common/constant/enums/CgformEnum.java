package org.jeecg.common.constant.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * online表单枚举 代码生成器用到
 */
public enum CgformEnum {

    /**
     * 单表
     */
    ONE(1, "one", "/jeecg/code-template-online", "default.one", "经典风格"),
    /**
     * 多表
     */
    MANY(2, "many", "/jeecg/code-template-online", "default.onetomany", "经典风格"),
    /**
     * 多表
     */
    ERP(2, "erp", "/jeecg/code-template-online", "erp.onetomany", "ERP风格"),
    /**
     * 多表（内嵌子表风格）
     */
    INNER_TABLE(2, "innerTable", "/jeecg/code-template-online", "inner-table.onetomany", "内嵌子表风格"),
    /**
     * 树形列表
     */
    TREE(3, "tree", "/jeecg/code-template-online", "default.tree", "树形列表");

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
     * 构造器
     *
     * @param type
     * @param code
     * @param templatePath
     * @param note
     */
    CgformEnum(int type, String code, String templatePath, String stylePath, String note) {
        this.type = type;
        this.code = code;
        this.templatePath = templatePath;
        this.stylePath = stylePath;
        this.note = note;
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
