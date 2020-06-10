package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.CompanyDynamicSupervision;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.vo.CompanyDynamicSupervisionVO;

import java.util.List;

/**
 * @Description: 企业年度动态监管
 * @Author: jeecg-boot
 * @Date:   2020-05-27
 * @Version: V1.0
 */
public interface CompanyDynamicSupervisionMapper extends BaseMapper<CompanyDynamicSupervision> {
    List<CompanyDynamicSupervisionVO> getCompanyDynamicSupervision(Page page, String companyId,String status, String companyName,String reportYear);
}
