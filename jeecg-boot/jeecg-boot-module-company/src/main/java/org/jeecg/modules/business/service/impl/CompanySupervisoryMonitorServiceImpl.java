package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.business.entity.CompanySupervisoryMonitor;
import org.jeecg.modules.business.mapper.CompanySupervisoryMonitorMapper;
import org.jeecg.modules.business.service.ICompanySupervisoryMonitorService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 监督性监测信息
 * @Author: jeecg-boot
 * @Date:   2020-06-01
 * @Version: V1.0
 */
@Service
public class CompanySupervisoryMonitorServiceImpl extends ServiceImpl<CompanySupervisoryMonitorMapper, CompanySupervisoryMonitor> implements ICompanySupervisoryMonitorService {

    @Override
    public Integer findCountByCompanyId(String companyId) {
        QueryWrapper<CompanySupervisoryMonitor> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CompanySupervisoryMonitor::getCompanyId,companyId);
        return this.count(queryWrapper);
    }
}
