package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.business.entity.CompanyDynamicSupervision;
import org.jeecg.modules.business.mapper.CompanyDynamicSupervisionMapper;
import org.jeecg.modules.business.service.ICompanyDynamicSupervisionService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 企业年度动态监管
 * @Author: jeecg-boot
 * @Date:   2020-05-27
 * @Version: V1.0
 */
@Service
public class CompanyDynamicSupervisionServiceImpl extends ServiceImpl<CompanyDynamicSupervisionMapper, CompanyDynamicSupervision> implements ICompanyDynamicSupervisionService {

    @Override
    public Integer findCountByCompanyId(String companyId) {
        QueryWrapper<CompanyDynamicSupervision> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CompanyDynamicSupervision::getCompanyId,companyId);
        return this.count(queryWrapper);
    }
}
