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
import java.util.Map;

public interface IDataCareService extends JeecgService<BlDataCare> {

    List<BlDataCare> getAllBlnoByErp(QueryWrapper<BlDataCare> queryWrapper);

    List<Map<String,Object>> getTmsBlNoDatas(QueryWrapper<BlDataCare> queryWrapper);

}
