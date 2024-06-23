package org.jeecg.common.util.sqlInjection;

import org.jeecg.common.exception.JeecgSqlInjectionException;
import org.jeecg.common.util.sqlInjection.parse.ParserSupport;
;

/**
 * SQL注入攻击分析器
 *
 * @author guyadong
 * 参考:
 * https://blog.csdn.net/10km/article/details/127767358
 * https://gitee.com/l0km/sql2java/tree/dev/sql2java-manager/src/main/java/gu/sql2java/parser
 */
public class SqlInjectionAnalyzer {

    //启用/关闭注入攻击检查
    private boolean injectCheckEnable = true;
    //防止SQL注入攻击分析实现
    private final InjectionSyntaxObjectAnalyzer injectionChecker;
    private final InjectionAstNodeVisitor injectionVisitor;

    public SqlInjectionAnalyzer() {
        this.injectionChecker = new InjectionSyntaxObjectAnalyzer();
        this.injectionVisitor = new InjectionAstNodeVisitor();
    }

    /**
     * 启用/关闭注入攻击检查,默认启动
     *
     * @param enable
     * @return
     */
    public SqlInjectionAnalyzer injectCheckEnable(boolean enable) {
        injectCheckEnable = enable;
        return this;
    }

    /**
     * 对解析后的SQL对象执行注入攻击分析，有注入攻击的危险则抛出异常{@link JeecgSqlInjectionException}
     *
     * @param sqlParserInfo
     * @throws JeecgSqlInjectionException
     */
    public ParserSupport.SqlParserInfo injectAnalyse(ParserSupport.SqlParserInfo sqlParserInfo) throws JeecgSqlInjectionException {
        if (null != sqlParserInfo && injectCheckEnable) {
            /** SQL注入攻击检查 */
            sqlParserInfo.statement.accept(injectionChecker);
            sqlParserInfo.simpleNode.jjtAccept(injectionVisitor, null);
        }
        return sqlParserInfo;
    }

    /**
     * sql校验
     */
    public static void checkSql(String sql,boolean check){
        SqlInjectionAnalyzer sqlInjectionAnalyzer = new SqlInjectionAnalyzer();
        sqlInjectionAnalyzer.injectCheckEnable(check);
        ParserSupport.SqlParserInfo sqlParserInfo = ParserSupport.parse0(sql, null,null);
        sqlInjectionAnalyzer.injectAnalyse(sqlParserInfo);
    }
}


