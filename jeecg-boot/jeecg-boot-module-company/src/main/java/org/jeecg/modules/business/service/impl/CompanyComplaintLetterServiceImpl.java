package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.business.entity.CompanyComplaintLetter;
import org.jeecg.modules.business.mapper.CompanyComplaintLetterMapper;
import org.jeecg.modules.business.service.ICompanyComplaintLetterService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 信访投诉信息
 * @Author: jeecg-boot
 * @Date:   2020-06-02
 * @Version: V1.0
 */
@Service
public class CompanyComplaintLetterServiceImpl extends ServiceImpl<CompanyComplaintLetterMapper, CompanyComplaintLetter> implements ICompanyComplaintLetterService {

    @Override
    public Integer findCountByCompanyId(String companyId) {
        QueryWrapper<CompanyComplaintLetter> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CompanyComplaintLetter::getCompanyId,companyId);
        return this.count(queryWrapper);
    }
}
