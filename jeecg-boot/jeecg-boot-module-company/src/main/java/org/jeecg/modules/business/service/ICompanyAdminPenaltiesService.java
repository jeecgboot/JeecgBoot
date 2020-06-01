package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.CompanyAdminPenalties;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 行政处罚信息
 * @Author: jeecg-boot
 * @Date:   2020-05-30
 * @Version: V1.0
 */
public interface ICompanyAdminPenaltiesService extends IService<CompanyAdminPenalties> {
    Integer findCountByCompanyId(String companyId);
}
