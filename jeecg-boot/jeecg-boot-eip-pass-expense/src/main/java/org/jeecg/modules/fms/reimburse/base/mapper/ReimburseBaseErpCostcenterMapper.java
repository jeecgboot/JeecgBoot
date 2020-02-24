package org.jeecg.modules.fms.reimburse.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.fms.reimburse.base.entity.ReimburseBaseErpCostcenter;
import org.jeecg.modules.fms.reimburse.base.vo.CostcenterVO;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: reimburse_base_erp_costcenter
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
public interface ReimburseBaseErpCostcenterMapper extends BaseMapper<ReimburseBaseErpCostcenter> {

	public List<CostcenterVO> queryCurCostCenterInfo(@Param("orgCode") String orgCode);
}
