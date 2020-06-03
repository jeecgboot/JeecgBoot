package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.business.entity.CompanyEnvTax;
import org.jeecg.modules.business.entity.CompanyPrevention;
import org.jeecg.modules.business.mapper.CompanyEnvTaxMapper;
import org.jeecg.modules.business.service.ICompanyEnvTaxService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 环保税信息
 * @Author: jeecg-boot
 * @Date: 2020-06-02
 * @Version: V1.0
 */
@Service
public class CompanyEnvTaxServiceImpl extends ServiceImpl<CompanyEnvTaxMapper, CompanyEnvTax> implements ICompanyEnvTaxService {

    /**
     * @Description:根据企业id查询数量
     * @Param:
     * @return:
     * @Author: 周志远
     * @Date: 2020/6/3
     */
    @Override
    public Integer findCountByCompanyId(String companyId) {
        QueryWrapper<CompanyEnvTax> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CompanyEnvTax::getCompanyId, companyId);
        return this.count(queryWrapper);
    }
}
