package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.business.entity.CompanyAcceptance;
import org.jeecg.modules.business.entity.CompanyApply;
import org.jeecg.modules.business.entity.CompanyBaseinfo;
import org.jeecg.modules.business.entity.CompanyPrevention;
import org.jeecg.modules.business.mapper.CompanyApplyMapper;
import org.jeecg.modules.business.service.ICompanyApplyService;
import org.jeecg.modules.business.utils.Constant;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 企业申报基础表
 * @Author: jeecg-boot
 * @Date: 2020-06-02
 * @Version: V1.0
 */
@Service
public class CompanyApplyServiceImpl extends ServiceImpl<CompanyApplyMapper, CompanyApply> implements ICompanyApplyService {

    /**
     * 根据刚提交基础信息表数据 存储申请基本表
     *
     * @param companyBaseinfo
     * @return
     */
    public boolean saveByBase(CompanyBaseinfo companyBaseinfo, String oldId) {
        CompanyApply companyApply = new CompanyApply();
        companyApply.setCompanyId(companyBaseinfo.getCompanyId());
        companyApply.setFromTable("company_baseinfo");
        companyApply.setNewId(companyBaseinfo.getId());
        companyApply.setStatus(Constant.status.PEND);
        companyApply.setId(oldId);
        return this.save(companyApply);
    }

    /**
     * 保存竣工验收信息申报记录
     *
     * @Description:
     * @Param:
     * @return:
     * @Author: 周志远
     * @Date: 2020/6/5
     */
    @Override
    public boolean saveByBase(CompanyAcceptance companyAcceptance, String oldId) {
        CompanyApply companyApply = new CompanyApply();
        companyApply.setCompanyId(companyAcceptance.getCompanyId());
        companyApply.setFromTable("company_acceptance");
        companyApply.setNewId(companyAcceptance.getId());
        companyApply.setStatus(companyAcceptance.getStatus());
        companyApply.setOldId(oldId);
        return this.save(companyApply);
    }

    /**
     * 保存污染防治信息申报记录
     *
     * @Description:
     * @Param:
     * @return:
     * @Author: 周志远
     * @Date: 2020/6/5
     */
    @Override
    public boolean saveByBase(CompanyPrevention companyPrevention) {
        CompanyApply companyApply = new CompanyApply();
        companyApply.setCompanyId(companyPrevention.getCompanyId());
        companyApply.setFromTable("company_prevention");
        companyApply.setNewId(companyPrevention.getId());
        companyApply.setStatus(Constant.status.PEND);
        companyApply.setId(companyPrevention.getOldId());
        return this.save(companyApply);
    }

    /**
     * @Description: 根据id和table查询申报记录
     * @Param:
     * @return:
     * @Author: 周志远
     * @Date: 2020/6/9
     */
    @Override
    public CompanyApply findByNewId(String id, String fromTable) {
        QueryWrapper<CompanyApply> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CompanyApply::getNewId, id).eq(CompanyApply::getFromTable, fromTable);
        return this.getOne(queryWrapper);
    }


}
