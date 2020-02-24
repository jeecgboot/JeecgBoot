package org.jeecg.modules.fms.reimburse.biz.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.fms.reimburse.biz.entity.ReimburseBizPaymentList;

/**
 * @Description: 报销单付款清单
 * @Author: jeecg-boot
 * @Date:   2020-01-08
 * @Version: V1.0
 */
public interface IReimburseBizPaymentListService extends IService<ReimburseBizPaymentList> {

	public List<ReimburseBizPaymentList> selectByMainId(String mainId);
}
