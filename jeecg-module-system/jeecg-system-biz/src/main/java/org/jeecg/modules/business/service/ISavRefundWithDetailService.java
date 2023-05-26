package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.entity.SavRefundWithDetail;

import java.util.List;

/**
 * @Description: 售后退款
 * @Author: jeecg-boot
 * @Date: 2022-08-16
 * @Version: V1.0
 */
public interface ISavRefundWithDetailService extends IService<SavRefundWithDetail> {
    List<SavRefundWithDetail> findUnprocessedRefundsByClient(String clientId);
}
