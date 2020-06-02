package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.CompanyComplaintLetter;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 信访投诉信息
 * @Author: jeecg-boot
 * @Date:   2020-06-02
 * @Version: V1.0
 */
public interface ICompanyComplaintLetterService extends IService<CompanyComplaintLetter> {
    Integer findCountByCompanyId(String companyId);
}
