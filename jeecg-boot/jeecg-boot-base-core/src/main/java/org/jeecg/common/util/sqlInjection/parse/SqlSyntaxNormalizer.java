package org.jeecg.common.util.sqlInjection.parse;

import net.sf.jsqlparser.util.TablesNamesFinder;

/**
 *  SQL语句分析转换器基类<br>
 * 基于SQL语法对象实现对SQL的修改
 * （暂时用不到）
 *
 * @author guyadong
 * @since 3.17.0
 */
public class SqlSyntaxNormalizer extends TablesNamesFinder {
    protected static final ThreadLocal<Boolean> changed = new ThreadLocal<>();

    public SqlSyntaxNormalizer() {
        super();
        init(true);

    }

    /**
     * 语句改变返回{@code true},否则返回{@code false}
     */
    public boolean changed() {
        return Boolean.TRUE.equals(changed.get());
    }

    /**
     * 复位线程局部变量{@link #changed}状态
     */
    public SqlSyntaxNormalizer resetChanged() {
        changed.remove();
        return this;
    }
}

