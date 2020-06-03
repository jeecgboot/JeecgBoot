package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.entity.CompanyEnvTax;

/**
 * @Description: 环保税信息
 * @Author: jeecg-boot
 * @Date: 2020-06-02
 * @Version: V1.0
 */
public interface ICompanyEnvTaxService extends IService<CompanyEnvTax> {
    /**
     * @Description:根据企业id查询数量
     * @Param:
     * @return:
     * @Author: 周志远
     * @Date: 2020/6/3
     */
    Integer findCountByCompanyId(String companyId);
}
