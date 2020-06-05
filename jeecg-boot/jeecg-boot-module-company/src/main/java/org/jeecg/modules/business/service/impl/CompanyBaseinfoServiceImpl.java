package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.jeecg.modules.business.entity.CompanyBaseinfo;
import org.jeecg.modules.business.mapper.CompanyBaseinfoMapper;
import org.jeecg.modules.business.service.ICompanyBaseinfoService;
import org.jeecg.modules.business.utils.Constant;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: company_baseinfo
 * @Author: jeecg-boot
 * @Date:   2020-05-27
 * @Version: V1.0
 */
@Service
public class CompanyBaseinfoServiceImpl extends ServiceImpl<CompanyBaseinfoMapper, CompanyBaseinfo> implements ICompanyBaseinfoService {
   public  CompanyBaseinfo queryByCompanyId(String companyId){
       return this.getOne((new QueryWrapper<CompanyBaseinfo>().lambda()
               .eq(CompanyBaseinfo :: getStatus, Constant.status.NORMAL)
               .eq(CompanyBaseinfo :: getCompanyId, companyId)));
   }

    /**
     *   根据公司id 修改状态
     * @param companyId
     * @return
     */
    public  boolean upDateStatus(String companyId,String status){
        UpdateWrapper<CompanyBaseinfo> updateWrapper = new UpdateWrapper<CompanyBaseinfo>()
                .eq("COMPANY_ID" , companyId).set("STATUS",status);
        return this.update(updateWrapper);
    }

}
