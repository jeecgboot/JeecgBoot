package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.business.entity.CompanyDirtyAllow;
import org.jeecg.modules.business.entity.CompanyPrevention;
import org.jeecg.modules.business.mapper.CompanyDirtyAllowMapper;
import org.jeecg.modules.business.service.ICompanyDirtyAllowService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 排污许可证信息
 * @Author: jeecg-boot
 * @Date: 2020-06-01
 * @Version: V1.0
 */
@Service
public class CompanyDirtyAllowServiceImpl extends ServiceImpl<CompanyDirtyAllowMapper, CompanyDirtyAllow> implements ICompanyDirtyAllowService {

    /**
     * @Description:根据企业id查询排污许可证信息数量
     * @Param:companyId
     * @return:Integer
     * @Author: 周志远
     * @Date: 2020/6/1
     */
    @Override
    public Integer findCountByCompanyId(String companyId) {
        QueryWrapper<CompanyDirtyAllow> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CompanyDirtyAllow::getCompanyId, companyId);
        return this.count(queryWrapper);
    }
}
