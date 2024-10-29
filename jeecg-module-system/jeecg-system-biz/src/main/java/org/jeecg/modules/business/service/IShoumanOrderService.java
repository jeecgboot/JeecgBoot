package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.entity.Shouman.ShoumanOrder;

import java.util.List;

/**
 * @Description: 首曼订单
 * @Author: jeecg-boot
 * @Date:   2023-11-29
 * @Version: V1.0
 */
public interface IShoumanOrderService extends IService<ShoumanOrder> {

    List<ShoumanOrder> findShoumanOrderToSend();
}
