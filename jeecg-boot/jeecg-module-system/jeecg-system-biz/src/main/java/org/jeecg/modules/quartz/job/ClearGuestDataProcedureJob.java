package org.jeecg.modules.quartz.job;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.system.vo.DynamicDataSourceModel;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.dynamic.db.DataSourceCachePool;
import org.jeecg.common.util.dynamic.db.DynamicDBUtil;
import org.jeecg.config.LogUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 清理测试数据
 */
@Slf4j
public class ClearGuestDataProcedureJob implements Job {
    private static final String jeecg = "jeecg-boot";
    private static final String shop = "bomaos-shop";
    private static final String alist = "alist";

    /**
     * 执行存储过程
     *
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Job Execution key：" + jobExecutionContext.getJobDetail().getKey());
        LogUtil.startTime("ClearGuestDataProcedureJob");
        try {
            String name = "Alist资源访问密码";
            // 清理码商城
            String sql1 = "update sys_article set enabled = 0 where username!='admin'";
            String sql2 = "update sys_products set status = 0 where username!='admin'";
            String sql3 = "update sys_classifys set status = 0 where username!='admin'";
            DynamicDBUtil.execute(shop, sql1,sql2,sql3);
            String query = "select max(pay_time) from sys_orders where product_name='"+name+"'";
            String date = DynamicDBUtil.getOne(shop,query);
            if (!DateUtils.isSameDay(DateUtils.getDate(),DateUtils.parseDatetime(date))) {
                // 更改访问密码
                String password = PasswordUtil.randomNum(6);
                sql1 = "update sys_cards set card_info='"+password+"' where product_id in(select p.id from sys_products p where p.name='"+name+"')";
                sql2 = "update x_meta set password='"+password+"' where password!='' and password is not null";
                DynamicDBUtil.execute(shop, sql1);
                DynamicDataSourceModel dbSource = DataSourceCachePool.getCacheDynamicDataSourceModel(alist);
                if (StringUtils.isBlank(dbSource.getDbUrl())) {
                    DruidDataSource dataSource = new DruidDataSource();
                    dataSource.setDriverClassName("org.sqlite.JDBC");
                    dataSource.setUrl("jdbc:sqlite:C:\\Users\\Administrator\\AppData\\Roaming\\shaoxia.xyz.sha1\\little_lucky2\\alist\\data.db");
                    DataSourceCachePool.putCacheBasicDataSource(alist, dataSource);
                }
                DynamicDBUtil.execute(alist, sql2);
            }
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
        LogUtil.endTime("CallAListProcedureJob");
    }
}
