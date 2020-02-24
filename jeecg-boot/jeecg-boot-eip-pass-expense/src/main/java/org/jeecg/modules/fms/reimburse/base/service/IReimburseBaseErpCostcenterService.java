package org.jeecg.modules.fms.reimburse.base.service;

import org.jeecg.modules.fms.reimburse.base.entity.ReimburseBaseErpCostcenter;
import org.jeecg.modules.fms.reimburse.base.vo.CostcenterVO;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: reimburse_base_erp_costcenter
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
public interface IReimburseBaseErpCostcenterService extends IService<ReimburseBaseErpCostcenter> {

	public CostcenterVO initCostCenterInfo(String orgCode);
}
