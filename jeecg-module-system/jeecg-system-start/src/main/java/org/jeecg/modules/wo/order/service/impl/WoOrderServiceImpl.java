package org.jeecg.modules.wo.order.service.impl;

import org.jeecg.modules.wo.order.entity.WoOrder;
import org.jeecg.modules.wo.order.entity.WoOrderDetail;
import org.jeecg.modules.wo.order.mapper.WoOrderDetailMapper;
import org.jeecg.modules.wo.order.mapper.WoOrderMapper;
import org.jeecg.modules.wo.order.service.IWoOrderService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 订单管理
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
@Service
public class WoOrderServiceImpl extends ServiceImpl<WoOrderMapper, WoOrder> implements IWoOrderService {

	@Autowired
	private WoOrderMapper woOrderMapper;
	@Autowired
	private WoOrderDetailMapper woOrderDetailMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(WoOrder woOrder, List<WoOrderDetail> woOrderDetailList) {
		woOrderMapper.insert(woOrder);
		for(WoOrderDetail entity:woOrderDetailList) {
			//外键设置
			entity.setOrderId(woOrder.getId());
			woOrderDetailMapper.insert(entity);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(WoOrder woOrder,List<WoOrderDetail> woOrderDetailList) {
		woOrderMapper.updateById(woOrder);
		
		//1.先删除子表数据
		woOrderDetailMapper.deleteByMainId(woOrder.getId().toString());
		
		//2.子表数据重新插入
		for(WoOrderDetail entity:woOrderDetailList) {
			//外键设置
			entity.setOrderId(woOrder.getId());
			woOrderDetailMapper.insert(entity);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		woOrderDetailMapper.deleteByMainId(id);
		woOrderMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			woOrderDetailMapper.deleteByMainId(id.toString());
			woOrderMapper.deleteById(id);
		}
	}
	
}
