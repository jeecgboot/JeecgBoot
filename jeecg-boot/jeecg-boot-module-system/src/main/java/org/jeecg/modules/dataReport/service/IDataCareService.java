package org.jeecg.modules.dataReport.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.common.system.base.service.JeecgService;
import org.jeecg.modules.dataReport.entity.BlDataCare;

import java.util.Date;
import java.util.List;

public interface IDataCareService extends JeecgService<BlDataCare> {

    IPage<BlDataCare> getAllBlnoByErp(Page<BlDataCare> page,QueryWrapper<BlDataCare> queryWrapper);

    List<String> getTmsBlNoDatas(QueryWrapper<BlDataCare> queryWrapper);

}
