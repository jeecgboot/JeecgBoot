package org.jeecg.common.util.sqlInjection.parse;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseLeftShift;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseRightShift;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.IntegerDivision;
import net.sf.jsqlparser.expression.operators.arithmetic.Modulo;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.conditional.XorExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.FullTextSearch;
import net.sf.jsqlparser.expression.operators.relational.GeometryDistance;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsBooleanExpression;
import net.sf.jsqlparser.expression.operators.relational.IsDistinctExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.expression.operators.relational.JsonOperator;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.expression.operators.relational.NamedExpressionList;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperator;
import net.sf.jsqlparser.expression.operators.relational.RegExpMySQLOperator;
import net.sf.jsqlparser.expression.operators.relational.SimilarToExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.SubSelect;

/**
 * 判断表达是否为常量的分析器
 *
 * @author guyadong
 */
public class ConstAnalyzer implements ExpressionVisitor, ItemsListVisitor {

    private static ThreadLocal<Boolean> constFlag = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return true;
        }
    };

    @Override
    public void visit(NullValue value) {
    }

    @Override
    public void visit(Function function) {
        constFlag.set(false);
    }

    @Override
    public void visit(SignedExpression expr) {
        expr.getExpression().accept(this);
    }

    @Override
    public void visit(JdbcParameter parameter) {
        constFlag.set(false);
    }

    @Override
    public void visit(JdbcNamedParameter parameter) {
        constFlag.set(false);
    }

    @Override
    public void visit(DoubleValue value) {

    }

    @Override
    public void visit(LongValue value) {

    }

    @Override
    public void visit(DateValue value) {

    }

    @Override
    public void visit(TimeValue value) {

    }

    @Override
    public void visit(TimestampValue value) {

    }

    @Override
    public void visit(Parenthesis parenthesis) {
        parenthesis.getExpression().accept(this);
    }

    @Override
    public void visit(StringValue value) {

    }

    @Override
    public void visit(Addition expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(Division expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(IntegerDivision expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(Multiplication expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(Subtraction expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(AndExpression expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(OrExpression expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(XorExpression expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(Between expr) {
        expr.getLeftExpression().accept(this);
        expr.getBetweenExpressionStart().accept(this);
        expr.getBetweenExpressionEnd().accept(this);
    }

    /**
     * 用于处理 OverlapsCondition 类型的表达式
     * @param overlapsCondition
     */
    @Override
    public void visit(OverlapsCondition overlapsCondition) {
        constFlag.set(false);
    }
    /**
     * 用于处理 SafeCastExpression 类型的表达式。
     * @param safeCastExpression
     */
    @Override
    public void visit(SafeCastExpression safeCastExpression) {
        constFlag.set(false);
    }

    @Override
    public void visit(EqualsTo expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(GreaterThan expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(GreaterThanEquals expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(InExpression expr) {
        if (expr.getLeftExpression() != null) {
            expr.getLeftExpression().accept(this);
        }
    }

    @Override
    public void visit(IsNullExpression expr) {
        expr.getLeftExpression().accept(this);
    }

    @Override
    public void visit(FullTextSearch expr) {
        constFlag.set(false);
    }

    @Override
    public void visit(IsBooleanExpression expr) {
        expr.getLeftExpression().accept(this);
    }

    @Override
    public void visit(LikeExpression expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(MinorThan expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(MinorThanEquals expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(NotEqualsTo expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(Column column) {
        if (!ParserSupport.isBoolean(column)) {
            constFlag.set(false);
        }
    }

    @Override
    public void visit(SubSelect subSelect) {
        constFlag.set(false);
    }

    @Override
    public void visit(CaseExpression expr) {
        constFlag.set(false);
    }

    @Override
    public void visit(WhenClause expr) {
        constFlag.set(false);
    }

    @Override
    public void visit(ExistsExpression expr) {
        constFlag.set(false);
    }

    @Override
    public void visit(AnyComparisonExpression expr) {
        constFlag.set(false);
    }

    @Override
    public void visit(Concat expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(Matches expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(BitwiseAnd expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(BitwiseOr expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(BitwiseXor expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(CastExpression expr) {
        expr.getLeftExpression().accept(this);
    }

    @Override
    public void visit(TryCastExpression expr) {
        constFlag.set(false);
    }

    @Override
    public void visit(Modulo expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(AnalyticExpression expr) {
        constFlag.set(false);
    }

    @Override
    public void visit(ExtractExpression expr) {
        expr.getExpression().accept(this);
    }

    @Override
    public void visit(IntervalExpression expr) {
        constFlag.set(false);
    }

    @Override
    public void visit(OracleHierarchicalExpression expr) {
        constFlag.set(false);
    }

    @Override
    public void visit(RegExpMatchOperator expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(ExpressionList expressionList) {
        for (Expression expr : expressionList.getExpressions()) {
            expr.accept(this);
        }
    }

    @Override
    public void visit(NamedExpressionList namedExpressionList) {
        for (Expression expr : namedExpressionList.getExpressions()) {
            expr.accept(this);
        }
    }

    @Override
    public void visit(MultiExpressionList multiExprList) {
        for (ExpressionList list : multiExprList.getExpressionLists()) {
            visit(list);
        }
    }

    @Override
    public void visit(NotExpression notExpr) {
        notExpr.getExpression().accept(this);
    }

    @Override
    public void visit(BitwiseRightShift expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(BitwiseLeftShift expr) {
        visitBinaryExpression(expr);
    }

    protected void visitBinaryExpression(BinaryExpression expr) {
        expr.getLeftExpression().accept(this);
        expr.getRightExpression().accept(this);
    }

    @Override
    public void visit(JsonExpression jsonExpr) {
        jsonExpr.getExpression().accept(this);
    }

    @Override
    public void visit(JsonOperator expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(RegExpMySQLOperator expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(UserVariable var) {
        constFlag.set(false);
    }

    @Override
    public void visit(NumericBind bind) {
        constFlag.set(false);
    }

    @Override
    public void visit(KeepExpression expr) {
        for (OrderByElement element : expr.getOrderByElements()) {
            element.getExpression().accept(this);
        }
    }

    @Override
    public void visit(MySQLGroupConcat groupConcat) {
        constFlag.set(false);
    }

    @Override
    public void visit(ValueListExpression valueListExpression) {
        for (Expression expr : valueListExpression.getExpressionList().getExpressions()) {
            expr.accept(this);
        }
    }

    @Override
    public void visit(AllColumns allColumns) {

    }

    @Override
    public void visit(AllTableColumns allTableColumns) {

    }

    @Override
    public void visit(AllValue allValue) {

    }

    @Override
    public void visit(IsDistinctExpression isDistinctExpression) {
        visitBinaryExpression(isDistinctExpression);
    }

    @Override
    public void visit(RowGetExpression rowGetExpression) {
        rowGetExpression.getExpression().accept(this);
    }

    @Override
    public void visit(HexValue hexValue) {

    }

    @Override
    public void visit(OracleHint hint) {

    }

    @Override
    public void visit(TimeKeyExpression timeKeyExpression) {

    }

    @Override
    public void visit(DateTimeLiteralExpression literal) {
    }

    @Override
    public void visit(NextValExpression nextVal) {
        constFlag.set(false);
    }

    @Override
    public void visit(CollateExpression col) {
        constFlag.set(false);
    }

    @Override
    public void visit(SimilarToExpression expr) {
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(ArrayExpression array) {
        array.getObjExpression().accept(this);
        if (array.getIndexExpression() != null) {
            array.getIndexExpression().accept(this);
        }
        if (array.getStartIndexExpression() != null) {
            array.getStartIndexExpression().accept(this);
        }
        if (array.getStopIndexExpression() != null) {
            array.getStopIndexExpression().accept(this);
        }
    }

    @Override
    public void visit(ArrayConstructor aThis) {
        for (Expression expression : aThis.getExpressions()) {
            expression.accept(this);
        }
    }

    @Override
    public void visit(VariableAssignment var) {
        constFlag.set(false);
    }

    @Override
    public void visit(XMLSerializeExpr expr) {
        constFlag.set(false);
    }

    @Override
    public void visit(TimezoneExpression expr) {
        expr.getLeftExpression().accept(this);
    }

    @Override
    public void visit(JsonAggregateFunction expression) {
        Expression expr = expression.getExpression();
        if (expr != null) {
            expr.accept(this);
        }

        expr = expression.getFilterExpression();
        if (expr != null) {
            expr.accept(this);
        }
    }

    @Override
    public void visit(JsonFunction expression) {
        for (JsonFunctionExpression expr : expression.getExpressions()) {
            expr.getExpression().accept(this);
        }
    }

    @Override
    public void visit(ConnectByRootOperator connectByRootOperator) {
        constFlag.set(false);
    }

    @Override
    public void visit(OracleNamedFunctionParameter oracleNamedFunctionParameter) {
        constFlag.set(false);
    }

    @Override
    public void visit(GeometryDistance geometryDistance) {
        visitBinaryExpression(geometryDistance);
    }

    @Override
    public void visit(RowConstructor rowConstructor) {
        constFlag.set(false);
    }

    public boolean isConstExpression(Expression expression) {
        if (null != expression) {
            constFlag.set(true);
            expression.accept(this);
            return constFlag.get();
        }
        return false;
    }
}

