package org.jeecg.modules.system.service.impl;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.creator.druid.DruidDataSourceCreator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.dynamic.db.DataSourceCachePool;
import org.jeecg.modules.system.entity.SysDataSource;
import org.jeecg.modules.system.mapper.SysDataSourceMapper;
import org.jeecg.modules.system.service.ISysDataSourceService;
import org.jeecg.modules.system.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

/**
 * @Description: 多数据源管理
 * @Author: jeecg-boot
 * @Date: 2019-12-25
 * @Version: V1.0
 */
@Service
public class SysDataSourceServiceImpl extends ServiceImpl<SysDataSourceMapper, SysDataSource> implements ISysDataSourceService {

    @Autowired
    private DruidDataSourceCreator dataSourceCreator;

    @Autowired
    private DataSource dataSource;

    @Override
    public Result saveDataSource(SysDataSource sysDataSource) {
        try {
            long count = checkDbCode(sysDataSource.getCode());
            if (count > 0) {
                return Result.error("数据源编码已存在");
            }
            String dbPassword = sysDataSource.getDbPassword();
            if (StringUtils.isNotBlank(dbPassword)) {
                String encrypt = SecurityUtil.jiami(dbPassword);
                sysDataSource.setDbPassword(encrypt);
            }
            boolean result = save(sysDataSource);
            if (result) {
                //动态创建数据源
                //addDynamicDataSource(sysDataSource, dbPassword);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.OK("添加成功！");
    }

    @Override
    public Result editDataSource(SysDataSource sysDataSource) {
        try {
            SysDataSource d = getById(sysDataSource.getId());
            DataSourceCachePool.removeCache(d.getCode());
            String dbPassword = sysDataSource.getDbPassword();
            if (StringUtils.isNotBlank(dbPassword)) {
                String encrypt = SecurityUtil.jiami(dbPassword);
                sysDataSource.setDbPassword(encrypt);
            }
            Boolean result=updateById(sysDataSource);
            if(result){
                //先删除老的数据源
               // removeDynamicDataSource(d.getCode());
                //添加新的数据源
                //addDynamicDataSource(sysDataSource,dbPassword);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.OK("编辑成功!");
    }

    @Override
    public Result deleteDataSource(String id) {
        SysDataSource sysDataSource = getById(id);
        DataSourceCachePool.removeCache(sysDataSource.getCode());
        removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 动态添加数据源 【注册mybatis动态数据源】
     *
     * @param sysDataSource 添加数据源数据对象
     * @param dbPassword    未加密的密码
     */
    private void addDynamicDataSource(SysDataSource sysDataSource, String dbPassword) {
        DataSourceProperty dataSourceProperty = new DataSourceProperty();
        dataSourceProperty.setUrl(sysDataSource.getDbUrl());
        dataSourceProperty.setPassword(dbPassword);
        dataSourceProperty.setDriverClassName(sysDataSource.getDbDriver());
        dataSourceProperty.setUsername(sysDataSource.getDbUsername());
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        DataSource dataSource = dataSourceCreator.createDataSource(dataSourceProperty);
        try {
            ds.addDataSource(sysDataSource.getCode(), dataSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除数据源
     * @param code
     */
    private void removeDynamicDataSource(String code) {
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        ds.removeDataSource(code);
    }

    /**
     * 检查数据源编码是否存在
     *
     * @param dbCode
     * @return
     */
    private long checkDbCode(String dbCode) {
        QueryWrapper<SysDataSource> qw = new QueryWrapper();
        qw.lambda().eq(true, SysDataSource::getCode, dbCode);
        return count(qw);
    }

}
