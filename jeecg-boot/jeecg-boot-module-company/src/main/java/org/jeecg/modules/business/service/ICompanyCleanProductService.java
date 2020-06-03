package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.CompanyCleanProduct;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 清洁生产
 * @Author: jeecg-boot
 * @Date: 2020-06-03
 * @Version: V1.0
 */
public interface ICompanyCleanProductService extends IService<CompanyCleanProduct> {
    /**
     * @Description:根据企业id查询数量
     * @Param:
     * @return:
     * @Author: 周志远
     * @Date: 2020/6/3
     */
    Integer findCountByCompanyId(String companyId);

}
