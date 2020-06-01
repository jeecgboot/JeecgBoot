package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.CompanyDynamicSupervision;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 企业年度动态监管
 * @Author: jeecg-boot
 * @Date:   2020-05-27
 * @Version: V1.0
 */
public interface ICompanyDynamicSupervisionService extends IService<CompanyDynamicSupervision> {
    Integer findCountByCompanyId(String companyId);
}
