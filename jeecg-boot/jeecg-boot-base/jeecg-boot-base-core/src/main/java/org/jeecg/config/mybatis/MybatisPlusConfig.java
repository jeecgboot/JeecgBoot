package org.jeecg.config.mybatis;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.parser.ISqlParserFilter;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import org.apache.ibatis.reflection.MetaObject;
import org.jeecg.common.util.oConvertUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 单数据源配置（jeecg.datasource.open = false时生效）
 * @Author zhoujf
 *
 */
@Configuration
@MapperScan(value={"org.jeecg.modules.**.mapper*"})
public class MybatisPlusConfig {

    /**
     * tenant_id 字段名
     */
    public static final String tenant_field = "tenant_id";

    /**
     * 有哪些表需要做多租户 这些表需要添加一个字段 ，字段名和tenant_field对应的值一样
     */
    private static final List<String> tenantTable = new ArrayList<String>();
    /**
     * ddl 关键字 判断不走多租户的sql过滤
     */
    private static final List<String> DDL_KEYWORD = new ArrayList<String>();
    static {
        tenantTable.add("jee_bug_danbiao");
        DDL_KEYWORD.add("alter");
    }

    /**
     * 多租户属于 SQL 解析部分，依赖 MP 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor().setLimit(-1);
        //多租户配置 配置后每次执行sql会走一遍他的转化器 如果不需要多租户功能 可以将其注释
        tenantConfig(paginationInterceptor);
        return paginationInterceptor;
    }

    /**
     * 多租户的配置
     * @param paginationInterceptor
     */
    private void tenantConfig(PaginationInterceptor paginationInterceptor){
        /*
         * 【测试多租户】 SQL 解析处理拦截器<br>
         * 这里固定写成住户 1 实际情况你可以从cookie读取，因此数据看不到 【 麻花藤 】 这条记录（ 注意观察 SQL ）<br>
         */
        List<ISqlParser> sqlParserList = new ArrayList<>();
        TenantSqlParser tenantSqlParser = new JeecgTenantParser();
        tenantSqlParser.setTenantHandler(new TenantHandler() {

            @Override
            public Expression getTenantId(boolean select) {
                String tenant_id = oConvertUtils.getString(TenantContext.getTenant(),"0");
                return new LongValue(tenant_id);
            }
            @Override
            public String getTenantIdColumn() {
                return tenant_field;
            }

            @Override
            public boolean doTableFilter(String tableName) {
                //true则不加租户条件查询  false则加
                // return excludeTable.contains(tableName);
                if(tenantTable.contains(tableName)){
                    return false;
                }
                return true;
            }

            private Expression in(String ids){
                final InExpression inExpression = new InExpression();
                inExpression.setLeftExpression(new Column(getTenantIdColumn()));
                final ExpressionList itemsList = new ExpressionList();
                final List<Expression> inValues = new ArrayList<>(2);
                for(String id:ids.split(",")){
                    inValues.add(new LongValue(id));
                }
                itemsList.setExpressions(inValues);
                inExpression.setRightItemsList(itemsList);
                return inExpression;
            }

        });

        sqlParserList.add(tenantSqlParser);
        paginationInterceptor.setSqlParserList(sqlParserList);
        paginationInterceptor.setSqlParserFilter(new ISqlParserFilter() {
            @Override
            public boolean doFilter(MetaObject metaObject) {
                String sql = (String) metaObject.getValue(PluginUtils.DELEGATE_BOUNDSQL_SQL);
                for(String tableName: tenantTable){
                    String sql_lowercase  = sql.toLowerCase();
                    if(sql_lowercase.indexOf(tableName.toLowerCase())>=0){
                        for(String key: DDL_KEYWORD){
                            if(sql_lowercase.indexOf(key)>=0){
                                return true;
                            }
                        }
                        return false;
                    }
                }
                /*if ("mapper路径.方法名".equals(ms.getId())) {
                    //使用这种判断也可以避免走此过滤器
                    return true;
                }*/
                return true;
            }
        });
    }
//    /**
//     * mybatis-plus SQL执行效率插件【生产环境可以关闭】
//     */
//    @Bean
//    public PerformanceInterceptor performanceInterceptor() {
//        return new PerformanceInterceptor();
//    }

}
