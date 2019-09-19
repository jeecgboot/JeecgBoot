package org.jeecg.modules.dataReport.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.dataReport.entity.BlDataCare;
import org.jeecg.modules.message.entity.SysMessageTemplate;

import java.util.Date;
import java.util.List;

/**
 * @Description: 数据比对
 */
public interface DataCareMapper extends BaseMapper<BlDataCare> {

    IPage<BlDataCare> getAllBlnoByErp(Page<org.jeecg.modules.dataReport.entity.BlDataCare> page,@Param(Constants.WRAPPER) Wrapper<BlDataCare> userWrapper);

    List<String> getTmsBlNoDatas(Date startDate,Date endDate);
}
