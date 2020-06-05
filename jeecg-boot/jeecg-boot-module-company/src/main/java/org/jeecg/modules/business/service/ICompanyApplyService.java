package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.CompanyApply;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.entity.CompanyBaseinfo;

/**
 * @Description: 企业申报基础表
 * @Author: jeecg-boot
 * @Date:   2020-06-02
 * @Version: V1.0
 */
public interface ICompanyApplyService extends IService<CompanyApply> {

     boolean saveByBase(CompanyBaseinfo companyBaseinfo,String oldId);

}
