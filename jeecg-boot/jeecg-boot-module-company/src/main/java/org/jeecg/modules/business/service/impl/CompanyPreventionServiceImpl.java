package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.business.entity.CompanyAcceptance;
import org.jeecg.modules.business.entity.CompanyPrevention;
import org.jeecg.modules.business.mapper.CompanyPreventionMapper;
import org.jeecg.modules.business.service.ICompanyPreventionService;
import org.springframework.stereotype.Service;

/**
 * @Description: 污染防治信息
 * @Author: jeecg-boot
 * @Date: 2020-06-01
 * @Version: V1.0
 */
@Service
public class CompanyPreventionServiceImpl extends ServiceImpl<CompanyPreventionMapper, CompanyPrevention> implements ICompanyPreventionService {

    /**
     * @Description:
     * @Param:companyId
     * @return:Integer
     * @Author: 周志远
     * @Date: 2020/6/1
     */
    @Override
    public Integer findCountByCompanyId(String companyId) {
        QueryWrapper<CompanyPrevention> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CompanyPrevention::getCompanyId, companyId);
        return this.count(queryWrapper);
    }
}
