package org.jeecg.common.util.sqlInjection;

import net.sf.jsqlparser.parser.CCJSqlParserDefaultVisitor;
import net.sf.jsqlparser.parser.SimpleNode;
import net.sf.jsqlparser.statement.select.UnionOp;
import org.jeecg.common.exception.JeecgSqlInjectionException;

/**
 * 基于抽象语法树(AST)的注入攻击分析实现
 *
 * @author guyadong
 */
public class InjectionAstNodeVisitor extends CCJSqlParserDefaultVisitor {
    public InjectionAstNodeVisitor() {
    }

    /**
     * 处理禁止联合查询
     *
     * @param node
     * @param data
     * @return
     */
    @Override
    public Object visit(SimpleNode node, Object data) {
        Object value = node.jjtGetValue();
        if (value instanceof UnionOp) {
            throw new JeecgSqlInjectionException("DISABLE UNION");
        }
        return super.visit(node, data);
    }
}

