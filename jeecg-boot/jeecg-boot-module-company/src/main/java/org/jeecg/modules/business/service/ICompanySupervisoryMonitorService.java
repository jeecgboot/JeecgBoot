package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.CompanySupervisoryMonitor;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 监督性监测信息
 * @Author: jeecg-boot
 * @Date:   2020-06-01
 * @Version: V1.0
 */
public interface ICompanySupervisoryMonitorService extends IService<CompanySupervisoryMonitor> {
    Integer findCountByCompanyId(String companyId);
}
