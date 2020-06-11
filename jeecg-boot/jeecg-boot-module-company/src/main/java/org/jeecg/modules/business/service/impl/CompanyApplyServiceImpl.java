package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.business.entity.*;
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
     * 保存申报记录
     *
     * @param
     * @return
     */
    public boolean saveByBase(String companyId,String id,String status, String oldId,String fromTable) {
        CompanyApply companyApply = new CompanyApply();
        companyApply.setCompanyId(companyId);
        companyApply.setFromTable(fromTable);
        companyApply.setNewId(id);
        companyApply.setStatus(status);
        companyApply.setOldId(oldId);
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
