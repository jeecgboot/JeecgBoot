package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.business.entity.CompanyAdminPenalties;
import org.jeecg.modules.business.mapper.CompanyAdminPenaltiesMapper;
import org.jeecg.modules.business.service.ICompanyAdminPenaltiesService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 行政处罚信息
 * @Author: jeecg-boot
 * @Date:   2020-05-30
 * @Version: V1.0
 */
@Service
public class CompanyAdminPenaltiesServiceImpl extends ServiceImpl<CompanyAdminPenaltiesMapper, CompanyAdminPenalties> implements ICompanyAdminPenaltiesService {


    @Override
    public Integer findCountByCompanyId(String companyId) {
        QueryWrapper<CompanyAdminPenalties> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CompanyAdminPenalties::getCompanyId,companyId);
        return this.count(queryWrapper);
    }
}
