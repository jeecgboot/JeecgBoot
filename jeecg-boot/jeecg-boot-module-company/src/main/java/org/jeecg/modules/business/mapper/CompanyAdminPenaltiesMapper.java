package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.CompanyAdminPenalties;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.vo.CompanyAdminPenaltiesVO;
import org.jeecg.modules.business.vo.CompanyDynamicSupervisionVO;

import java.util.Date;
import java.util.List;

/**
 * @Description: 行政处罚信息
 * @Author: jeecg-boot
 * @Date:   2020-05-30
 * @Version: V1.0
 */
public interface CompanyAdminPenaltiesMapper extends BaseMapper<CompanyAdminPenalties> {
    List<CompanyAdminPenaltiesVO> getCompanyAdminPenalties(Page page, String companyId, String status, String companyName,Date dateBegin,Date dateEnd);
}
