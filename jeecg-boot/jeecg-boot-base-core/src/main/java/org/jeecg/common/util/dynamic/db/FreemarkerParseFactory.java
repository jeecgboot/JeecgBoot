package org.jeecg.common.util.dynamic.db;

import freemarker.cache.StringTemplateLoader;
import freemarker.core.ParseException;
import freemarker.core.TemplateClassResolver;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.constant.DataBaseConstant;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecgframework.codegenerate.generate.util.SimpleFormat;

import java.io.StringWriter;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author 赵俊夫
 * @version V1.0
 * @Title:FreemarkerHelper
 * @description:Freemarker引擎协助类
 * @date Jul 5, 2013 2:58:29 PM
 */
@Slf4j
public class FreemarkerParseFactory {

    private static final String ENCODE = "utf-8";
    /**
     * 参数格式化工具类
     */
    private static final String MINI_DAO_FORMAT = "DaoFormat";

    /**
     * 文件缓存
     */
    private static final Configuration TPL_CONFIG = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
    /**
     * SQL 缓存
     */
    private static final Configuration SQL_CONFIG = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);

    private static StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();

    /**使用内嵌的(?ms)打开单行和多行模式*/
    private final static Pattern NOTES_PATTERN = Pattern
            .compile("(?ms)/\\*.*?\\*/|^\\s*//.*?$");

    static {
        TPL_CONFIG.setClassForTemplateLoading(new FreemarkerParseFactory().getClass(), "/");
        TPL_CONFIG.setNumberFormat("0.#####################");
        SQL_CONFIG.setTemplateLoader(stringTemplateLoader);
        SQL_CONFIG.setNumberFormat("0.#####################");
        //classic_compatible设置，解决报空指针错误
        SQL_CONFIG.setClassicCompatible(true);

        //update-begin-author:taoyan date:2022-8-10 for: freemarker模板注入问题 禁止解析ObjectConstructor，Execute和freemarker.template.utility.JythonRuntime。
        //https://ackcent.com/in-depth-freemarker-template-injection/
        TPL_CONFIG.setNewBuiltinClassResolver(TemplateClassResolver.SAFER_RESOLVER);
        SQL_CONFIG.setNewBuiltinClassResolver(TemplateClassResolver.SAFER_RESOLVER);
        //update-end-author:taoyan date:2022-8-10 for: freemarker模板注入问题 禁止解析ObjectConstructor，Execute和freemarker.template.utility.JythonRuntime。
    }

    /**
     * 判断模板是否存在
     *
     * @throws Exception
     */
    public static boolean isExistTemplate(String tplName) throws Exception {
        try {
            Template mytpl = TPL_CONFIG.getTemplate(tplName, "UTF-8");
            if (mytpl == null) {
                return false;
            }
        } catch (Exception e) {
            //update-begin--Author:scott  Date:20180320 for：解决问题 - 错误提示sql文件不存在，实际问题是sql freemarker用法错误-----
            if (e instanceof ParseException) {
                log.error(e.getMessage(), e.fillInStackTrace());
                throw new Exception(e);
            }
            log.debug("----isExistTemplate----" + e.toString());
            //update-end--Author:scott  Date:20180320 for：解决问题 - 错误提示sql文件不存在，实际问题是sql freemarker用法错误------
            return false;
        }
        return true;
    }

    /**
     * 解析ftl模板
     *
     * @param tplName 模板名
     * @param paras   参数
     * @return
     */
    public static String parseTemplate(String tplName, Map<String, Object> paras) {
        try {
            log.debug(" minidao sql templdate : " + tplName);
            StringWriter swriter = new StringWriter();
            Template mytpl = TPL_CONFIG.getTemplate(tplName, ENCODE);
            if (paras.containsKey(MINI_DAO_FORMAT)) {
                throw new RuntimeException("DaoFormat 是 minidao 保留关键字，不允许使用 ，请更改参数定义！");
            }
            paras.put(MINI_DAO_FORMAT, new SimpleFormat());
            mytpl.process(paras, swriter);
            String sql = getSqlText(swriter.toString());
            paras.remove(MINI_DAO_FORMAT);
            return sql;
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            log.error("发送一次的模板key:{ " + tplName + " }");
            //System.err.println(e.getMessage());
            //System.err.println("模板名:{ "+ tplName +" }");
            throw new RuntimeException("解析SQL模板异常");
        }
    }

    /**
     * 解析ftl
     *
     * @param tplContent 模板内容
     * @param paras      参数
     * @return String 模板解析后内容
     */
    public static String parseTemplateContent(String tplContent,Map<String, Object> paras) {
        return parseTemplateContent(tplContent, paras, false);
    }
    public static String parseTemplateContent(String tplContent, Map<String, Object> paras, boolean keepSpace) {
        try {
            String sqlUnderline="sql_";
            StringWriter swriter = new StringWriter();
            if (stringTemplateLoader.findTemplateSource(sqlUnderline + tplContent.hashCode()) == null) {
                stringTemplateLoader.putTemplate(sqlUnderline + tplContent.hashCode(), tplContent);
            }
            Template mytpl = SQL_CONFIG.getTemplate(sqlUnderline + tplContent.hashCode(), ENCODE);
            if (paras.containsKey(MINI_DAO_FORMAT)) {
                throw new RuntimeException("DaoFormat 是 minidao 保留关键字，不允许使用 ，请更改参数定义！");
            }
            paras.put(MINI_DAO_FORMAT, new SimpleFormat());
            mytpl.process(paras, swriter);
            String sql = getSqlText(swriter.toString(), keepSpace);
            paras.remove(MINI_DAO_FORMAT);
            return sql;
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            log.error("发送一次的模板key:{ " + tplContent + " }");
            //System.err.println(e.getMessage());
            //System.err.println("模板内容:{ "+ tplContent +" }");
            throw new RuntimeException("解析SQL模板异常");
        }
    }

    /**
     * 除去无效字段，去掉注释 不然批量处理可能报错 去除无效的等于
     */
    private static String getSqlText(String sql) {
        return getSqlText(sql, false);
    }

    private static String getSqlText(String sql, boolean keepSpace) {
        // 将注释替换成""
        sql = NOTES_PATTERN.matcher(sql).replaceAll("");
        if (!keepSpace) {
            sql = sql.replaceAll("\\n", " ").replaceAll("\\t", " ")
                    .replaceAll("\\s{1,}", " ").trim();
        }
        // 去掉 最后是 where这样的问题
        //where空格 "where "
        String whereSpace = DataBaseConstant.SQL_WHERE+" ";
        //"where and"
        String whereAnd = DataBaseConstant.SQL_WHERE+" and";
        //", where"
        String commaWhere = SymbolConstant.COMMA+" "+DataBaseConstant.SQL_WHERE;
        //", "
        String commaSpace = SymbolConstant.COMMA + " ";
        if (sql.endsWith(DataBaseConstant.SQL_WHERE) || sql.endsWith(whereSpace)) {
            sql = sql.substring(0, sql.lastIndexOf("where"));
        }
        // 去掉where and 这样的问题
        int index = 0;
        while ((index = StringUtils.indexOfIgnoreCase(sql, whereAnd, index)) != -1) {
            sql = sql.substring(0, index + 5)
                    + sql.substring(index + 9, sql.length());
        }
        // 去掉 , where 这样的问题
        index = 0;
        while ((index = StringUtils.indexOfIgnoreCase(sql, commaWhere, index)) != -1) {
            sql = sql.substring(0, index)
                    + sql.substring(index + 1, sql.length());
        }
        // 去掉 最后是 ,这样的问题
        if (sql.endsWith(SymbolConstant.COMMA) || sql.endsWith(commaSpace)) {
            sql = sql.substring(0, sql.lastIndexOf(","));
        }
        return sql;
    }
}