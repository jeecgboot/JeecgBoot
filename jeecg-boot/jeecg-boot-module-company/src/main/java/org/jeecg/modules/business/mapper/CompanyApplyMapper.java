package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.CompanyApply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.vo.CompanyApplyVo;
import org.jeecg.modules.business.vo.QualificationBaseInfo;

import java.util.List;

/**
 * @Description: 企业申报基础表
 * @Author: jeecg-boot
 * @Date:   2020-06-02
 * @Version: V1.0
 */
public interface CompanyApplyMapper extends BaseMapper<CompanyApply> {


    /**
     * 通过公司id获取所有申报信息
     * @param companyIds 公司id
     * @return
     */
    public List<CompanyApplyVo> queryCompanyApplyVo(Page<CompanyApplyVo> page,String[] companyIds,String status,String fromTable);

}
