package org.jeecg.modules.order.service;

import org.jeecg.modules.order.entity.ResponseOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: Response Order
 * @Author: jeecg-boot
 * @Date:   2021-05-05
 * @Version: V1.0
 */
public interface IResponseOrderService extends IService<ResponseOrder> {

	public List<ResponseOrder> selectByMainId(String mainId);
}
