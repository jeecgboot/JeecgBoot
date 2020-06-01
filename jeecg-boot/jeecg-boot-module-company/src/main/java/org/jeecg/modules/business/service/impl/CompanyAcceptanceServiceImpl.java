package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.CompanyAcceptance;
import org.jeecg.modules.business.mapper.CompanyAcceptanceMapper;
import org.jeecg.modules.business.service.ICompanyAcceptanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 竣工验收信息
 * @Author: jeecg-boot
 * @Date: 2020-05-30
 * @Version: V1.0
 */
@Service
public class CompanyAcceptanceServiceImpl extends ServiceImpl<CompanyAcceptanceMapper, CompanyAcceptance> implements ICompanyAcceptanceService {

    /**
     * @Description:根据companyId查询数量
     * @Param:
     * @return:
     * @Author: 周志远
     * @Date: 2020/6/1
     */
    @Override
    public Integer findCountByCompanyId(String companyId) {
        QueryWrapper<CompanyAcceptance> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CompanyAcceptance::getCompanyId, companyId);
        return this.count(queryWrapper);
    }
}
