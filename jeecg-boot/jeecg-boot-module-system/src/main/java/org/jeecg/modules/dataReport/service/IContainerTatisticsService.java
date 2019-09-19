package org.jeecg.modules.dataReport.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.dataReport.entity.ContainerTatistics;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 集装箱业务统计表
 * @Author: jeecg-boot
 * @Date:   2019-08-16
 * @Version: V1.0
 */
public interface IContainerTatisticsService extends IService<ContainerTatistics> {

    IPage<ContainerTatistics> getContainerTatistics(Page<ContainerTatistics> page, QueryWrapper<ContainerTatistics> queryWrapper);

}
