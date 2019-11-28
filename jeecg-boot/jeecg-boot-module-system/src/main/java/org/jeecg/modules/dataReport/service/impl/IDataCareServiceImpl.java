package org.jeecg.modules.dataReport.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.system.base.service.impl.JeecgServiceImpl;
import org.jeecg.modules.dataReport.entity.BlDataCare;
import org.jeecg.modules.dataReport.mapper.DataCareMapper;
import org.jeecg.modules.dataReport.service.IDataCareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @date 2019-08-08
 */
@Service
public class IDataCareServiceImpl extends ServiceImpl<DataCareMapper, BlDataCare> implements IDataCareService {

    @Autowired
    DataCareMapper dataCareMapper;

    @Override
    @DS("erp")
    public List<BlDataCare> getAllBlnoByErp(QueryWrapper<BlDataCare> queryWrapper) {
        return dataCareMapper.getAllBlnoByErp(queryWrapper);
    }

    @Override
    @DS("tms")
    public List<Map<String,Object>> getTmsBlNoDatas(QueryWrapper<BlDataCare> queryWrapper) {
        Date startDate = (Date) queryWrapper.getParamNameValuePairs().get("MPGENVAL1");
        Date endDate = (Date) queryWrapper.getParamNameValuePairs().get("MPGENVAL2");
        return dataCareMapper.getTmsBlNoDatas(startDate,endDate);
    }
}
