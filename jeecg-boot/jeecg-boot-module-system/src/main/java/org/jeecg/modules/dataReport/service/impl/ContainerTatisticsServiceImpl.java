package org.jeecg.modules.dataReport.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.dataReport.entity.ContainerTatistics;
import org.jeecg.modules.dataReport.mapper.ContainerTatisticsMapper;
import org.jeecg.modules.dataReport.service.IContainerTatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 集装箱业务统计表
 * @Author: jeecg-boot
 * @Date:   2019-08-16
 * @Version: V1.0
 */
@Service
public class ContainerTatisticsServiceImpl extends ServiceImpl<ContainerTatisticsMapper, ContainerTatistics> implements IContainerTatisticsService {

    @Autowired
    ContainerTatisticsMapper containerTatisticsMapper;

    @Override
    @DS("tms")
    public IPage<ContainerTatistics> getContainerTatistics(Page<ContainerTatistics> page, QueryWrapper<ContainerTatistics> queryWrapper) {
        return containerTatisticsMapper.getContainerTatistics(page,queryWrapper);
    }
}
