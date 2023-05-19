package org.jeecg.modules.wo.order.service.impl;

import org.jeecg.modules.wo.order.entity.WoOrderDetail;
import org.jeecg.modules.wo.order.mapper.WoOrderDetailMapper;
import org.jeecg.modules.wo.order.service.IWoOrderDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 订单明细
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
@Service
public class WoOrderDetailServiceImpl extends ServiceImpl<WoOrderDetailMapper, WoOrderDetail> implements IWoOrderDetailService {
	
	@Autowired
	private WoOrderDetailMapper woOrderDetailMapper;
	
	@Override
	public List<WoOrderDetail> selectByMainId(String mainId) {
		return woOrderDetailMapper.selectByMainId(mainId);
	}
}
