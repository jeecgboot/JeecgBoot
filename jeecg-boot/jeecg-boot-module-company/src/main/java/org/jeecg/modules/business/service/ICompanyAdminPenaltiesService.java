package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.CompanyAdminPenalties;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.vo.CompanyAdminPenaltiesVO;

import java.util.Date;

/**
 * @Description: 行政处罚信息
 * @Author: jeecg-boot
 * @Date:   2020-05-30
 * @Version: V1.0
 */
public interface ICompanyAdminPenaltiesService extends IService<CompanyAdminPenalties> {
    Integer findCountByCompanyId(String companyId);
    Page<CompanyAdminPenaltiesVO> getCompanyAdminPenalties(Page<CompanyAdminPenaltiesVO> page, String companyId, String status, String companyName,Date dateBegin,Date dateEnd);
}
