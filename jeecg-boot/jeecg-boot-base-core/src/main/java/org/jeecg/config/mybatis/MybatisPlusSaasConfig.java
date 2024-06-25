package org.jeecg.config.mybatis;

import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.TenantConstant;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.TokenUtils;
import org.jeecg.common.util.oConvertUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;

/**
 * 单数据源配置（jeecg.datasource.open = false时生效）
 * @Author zhoujf
 *
 */
@Configuration
@MapperScan(value={"org.jeecg.**.mapper*"})
public class MybatisPlusSaasConfig {

    /**
     * 是否开启系统模块的租户隔离
     *  控制范围：用户、角色、部门、我的部门、字典、分类字典、多数据源、职务、通知公告
     *  
     *  实现功能
     *  1.用户表通过硬编码实现租户ID隔离
     *  2.角色、部门、我的部门、字典、分类字典、多数据源、职务、通知公告除了硬编码还加入的 TENANT_TABLE 配置中，实现租户隔离更安全
     *  3.菜单表、租户表不做租户隔离
     *  4.通过拦截器MybatisInterceptor实现，增删改查数据 自动注入租户ID
     */
    public static final Boolean OPEN_SYSTEM_TENANT_CONTROL = false;
    
    /**
     * 哪些表需要做多租户 表需要添加一个字段 tenant_id
     */
    public static final List<String> TENANT_TABLE = new ArrayList<String>();

    static {
        //1.需要租户隔离的表请在此配置
        if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
            //a.系统管理表
            //TENANT_TABLE.add("sys_role");
            //TENANT_TABLE.add("sys_user_role");
            TENANT_TABLE.add("sys_depart");
            TENANT_TABLE.add("sys_category");
            TENANT_TABLE.add("sys_data_source");
            TENANT_TABLE.add("sys_position");
            //TENANT_TABLE.add("sys_announcement");
        }

        //2.示例测试
        //TENANT_TABLE.add("demo");
        //3.online租户隔离测试
        //TENANT_TABLE.add("ceapp_issue");
    }


    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 先 add TenantLineInnerInterceptor 再 add PaginationInnerInterceptor
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                String tenantId = TenantContext.getTenant();
                //如果通过线程获取租户ID为空，则通过当前请求的request获取租户（shiro排除拦截器的请求会获取不到租户ID）
                if(oConvertUtils.isEmpty(tenantId)){
                    try {
                        tenantId = TokenUtils.getTenantIdByRequest(SpringContextUtils.getHttpServletRequest());
                    } catch (Exception e) {
                        //e.printStackTrace();
                    }
                }
                if(oConvertUtils.isEmpty(tenantId)){
                    tenantId = "0";
                }
                return new LongValue(tenantId);
            }

            @Override
            public String getTenantIdColumn(){
                return TenantConstant.TENANT_ID_TABLE;
            }

            // 返回 true 表示不走租户逻辑
            @Override
            public boolean ignoreTable(String tableName) {
                for(String temp: TENANT_TABLE){
                    if(temp.equalsIgnoreCase(tableName)){
                        return false;
                    }
                }
                return true;
            }
        }));
        //update-begin-author:zyf date:20220425 for:【VUEN-606】注入动态表名适配拦截器解决多表名问题
        interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor());
        //update-end-author:zyf date:20220425 for:【VUEN-606】注入动态表名适配拦截器解决多表名问题
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        //【jeecg-boot/issues/3847】增加@Version乐观锁支持 
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }

    /**
     * 动态表名切换拦截器,用于适配vue2和vue3同一个表有多个的情况,如sys_role_index在vue3情况下表名为sys_role_index_v3
     * @return
     */
    private DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor() {
        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        dynamicTableNameInnerInterceptor.setTableNameHandler((sql, tableName) -> {
            //获取需要动态解析的表名
            String dynamicTableName = ThreadLocalDataHelper.get(CommonConstant.DYNAMIC_TABLE_NAME);
            //当dynamicTableName不为空时才走动态表名处理逻辑,否则返回原始表名
            if (ObjectUtil.isNotEmpty(dynamicTableName) && dynamicTableName.equals(tableName)) {
                // 获取前端传递的版本号标识
                Object version = ThreadLocalDataHelper.get(CommonConstant.VERSION);
                if (ObjectUtil.isNotEmpty(version)) {
                    //拼接表名规则(原始表名+下划线+前端传递的版本号)
                    return tableName + "_" + version;
                }
            }
            return tableName;
        });
        return dynamicTableNameInnerInterceptor;
    }

//    /**
//     * 下个版本会删除，现在为了避免缓存出现问题不得不配置
//     * @return
//     */
//    @Bean
//    public ConfigurationCustomizer configurationCustomizer() {
//        return configuration -> configuration.setUseDeprecatedExecutor(false);
//    }
//    /**
//     * mybatis-plus SQL执行效率插件【生产环境可以关闭】
//     */
//    @Bean
//    public PerformanceInterceptor performanceInterceptor() {
//        return new PerformanceInterceptor();
//    }

}
