package org.jeecg.common.util.sqlInjection.parse;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Throwables;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.exception.JeecgSqlInjectionException;

/**
 * 解析sql支持
 */
@Slf4j
public class ParserSupport {
    /**
     * 解析SELECT SQL语句,解析失败或非SELECT语句则抛出异常
     *
     * @param sql
     * @return
     */
    public static Select parseSelect(String sql) {
        Statement stmt;
        try {
            stmt = CCJSqlParserUtil.parse(checkNotNull(sql, "sql is null"));
        } catch (JSQLParserException e) {
            throw new JeecgBootException(e);
        }
        checkArgument(stmt instanceof Select, "%s is not  SELECT statment", sql);
        Select select = (Select) stmt;
        SelectBody selectBody = select.getSelectBody();
        // 暂时只支持简单的SELECT xxxx FROM ....语句不支持复杂语句如WITH
        checkArgument(selectBody instanceof PlainSelect, "ONLY SUPPORT plain select statement %s", sql);
        return (Select) stmt;
    }

    /**
     * 解析SELECT SQL语句,解析失败或非SELECT语句则
     *
     * @param sql
     * @return
     */
    public static Select parseSelectUnchecked(String sql) {
        try {
            return parseSelect(sql);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 实现SQL语句解析,解析成功则返回解析后的{@link Statement}，
     * 并通过{@code visitor}参数提供基于AST(抽象语法树)的遍历所有节点的能力。
     *
     * @param sql                 SQL语句
     * @param visitor             遍历所有节点的{@link SimpleNodeVisitor}接口实例，为{@code null}忽略
     * @param sqlSyntaxNormalizer SQL语句分析转换器，为{@code null}忽略
     * @throws JSQLParserException 输入的SQL语句有语法错误
     * @see #parse0(String, CCJSqlParserVisitor, SqlSyntaxNormalizer)
     */
    public static Statement parse(String sql, CCJSqlParserVisitor visitor, SqlSyntaxNormalizer sqlSyntaxNormalizer) throws JSQLParserException {
        return parse0(sql, visitor, sqlSyntaxNormalizer).statement;
    }

    /**
     * 参照{@link CCJSqlParserUtil#parseAST(String)}和{@link CCJSqlParserUtil#parse(String)}实现SQL语句解析,
     * 解析成功则返回解析后的{@link SqlParserInfo}对象，
     * 并通过{@code visitor}参数提供基于AST(抽象语法树)的遍历所有节点的能力。
     *
     * @param sql               SQL语句
     * @param visitor           遍历所有节点的{@link SimpleNodeVisitor}接口实例，为{@code null}忽略
     * @param sqlSyntaxAnalyzer SQL语句分析转换器，为{@code null}忽略
     * @throws JSQLParserException 输入的SQL语句有语法错误
     * @see net.sf.jsqlparser.parser.Node#jjtAccept(SimpleNodeVisitor, Object)
     */
    public static SqlParserInfo parse0(String sql, CCJSqlParserVisitor visitor, SqlSyntaxNormalizer sqlSyntaxAnalyzer) throws JeecgSqlInjectionException {

        //检查是否非select开头，暂不支持
        if(!sql.toLowerCase().trim().startsWith("select ")) {
            log.warn("传入sql 非select开头，不支持非select开头的语句解析！");
            return null;
        }

        //检查是否存储过程，暂不支持
        if(sql.toLowerCase().trim().startsWith("call ")){
            log.warn("传入call 开头存储过程，不支持存储过程解析！");
            return null;
        }

        //检查特殊语义的特殊字符，目前检查冒号、$、#三种特殊语义字符
        String specialCharacters = "[:$#]";
        Pattern pattern = Pattern.compile(specialCharacters);
        Matcher matcher = pattern.matcher(sql);
        if (matcher.find()) {
            sql = sql.replaceAll("[:$#]", "@");
        }

        checkArgument(null != sql, "sql is null");
        boolean allowComplexParsing = CCJSqlParserUtil.getNestingDepth(sql) <= CCJSqlParserUtil.ALLOWED_NESTING_DEPTH;

        CCJSqlParser parser = CCJSqlParserUtil.newParser(sql).withAllowComplexParsing(allowComplexParsing);
        Statement stmt;
        try {
            stmt = parser.Statement();
        } catch (Exception ex) {
            log.error("请注意，SQL语法可能存在问题---> {}", ex.getMessage());
            throw new JeecgSqlInjectionException("请注意，SQL语法可能存在问题:"+sql);
        }
        if (null != visitor) {
            parser.getASTRoot().jjtAccept(visitor, null);
        }
        if (null != sqlSyntaxAnalyzer) {
            stmt.accept(sqlSyntaxAnalyzer.resetChanged());
        }
        return new SqlParserInfo(stmt.toString(), stmt, (SimpleNode) parser.getASTRoot());
    }

    /**
     * 调用{@link CCJSqlParser}解析SQL语句部件返回解析生成的对象,如{@code 'ORDER BY id DESC'}
     *
     * @param <T>
     * @param input
     * @param method     指定调用的{@link CCJSqlParser}解析方法
     * @param targetType 返回的解析对象类型
     * @return
     * @since 3.18.3
     */
    public static <T> T parseComponent(String input, String method, Class<T> targetType) {
        try {
            CCJSqlParser parser = new CCJSqlParser(new StringProvider(input));
            try {
                return checkNotNull(targetType, "targetType is null").cast(parser.getClass().getMethod(method).invoke(parser));
            } catch (InvocationTargetException e) {
                Throwables.throwIfUnchecked(e.getTargetException());
                throw new RuntimeException(e.getTargetException());
            }
        } catch (IllegalAccessException | NoSuchMethodException | SecurityException e) {
            Throwables.throwIfUnchecked(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 如果{@link Column}没有定义table,且字段名为true/false(不区分大小写)则视为布尔常量
     *
     * @param column
     */
    public static boolean isBoolean(Column column) {
        return null != column && null == column.getTable() &&
                Pattern.compile("(true|false)", Pattern.CASE_INSENSITIVE).matcher(column.getColumnName()).matches();
    }

    public static class SqlParserInfo {
        public String nativeSql;
        public Statement statement;
        public SimpleNode simpleNode;

        SqlParserInfo(String nativeSql, Statement statement, SimpleNode simpleNode) {
            this.nativeSql = nativeSql;
            this.statement = statement;
            this.simpleNode = simpleNode;
        }
    }
}

