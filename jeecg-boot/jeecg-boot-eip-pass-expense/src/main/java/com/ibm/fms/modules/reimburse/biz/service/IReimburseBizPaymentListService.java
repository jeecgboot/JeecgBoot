package com.ibm.fms.modules.reimburse.biz.service;

import com.ibm.fms.modules.reimburse.biz.entity.ReimburseBizPaymentList;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 报销单付款清单
 * @Author: jeecg-boot
 * @Date:   2020-01-08
 * @Version: V1.0
 */
public interface IReimburseBizPaymentListService extends IService<ReimburseBizPaymentList> {

	public List<ReimburseBizPaymentList> selectByMainId(String mainId);
}
