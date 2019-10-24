package org.jeecg.modules.dataReport.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.dataReport.entity.ContainerTatistics;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 集装箱业务统计表
 * @Author: Zhao
 * @Date:   2019-08-16
 * @Version: V1.0
 */
public interface ContainerTatisticsMapper extends BaseMapper<ContainerTatistics> {


    IPage<ContainerTatistics> getContainerTatistics(Page<ContainerTatistics> page, @Param(Constants.WRAPPER) Wrapper<ContainerTatistics> queryWrapper);

    List<ContainerTatistics> getContainerTatistics(@Param(Constants.WRAPPER) Wrapper<ContainerTatistics> queryWrapper);
}
