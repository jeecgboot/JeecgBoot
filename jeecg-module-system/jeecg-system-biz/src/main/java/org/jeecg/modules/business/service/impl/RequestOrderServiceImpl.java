package org.jeecg.modules.order.service.impl;

import org.jeecg.modules.order.entity.RequestOrder;
import org.jeecg.modules.order.entity.ResponseOrder;
import org.jeecg.modules.order.mapper.ResponseOrderMapper;
import org.jeecg.modules.order.mapper.RequestOrderMapper;
import org.jeecg.modules.order.service.IRequestOrderService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: Request Order
 * @Author: jeecg-boot
 * @Date:   2021-05-05
 * @Version: V1.0
 */
@Service
public class RequestOrderServiceImpl extends ServiceImpl<RequestOrderMapper, RequestOrder> implements IRequestOrderService {

	@Autowired
	private RequestOrderMapper requestOrderMapper;
	@Autowired
	private ResponseOrderMapper responseOrderMapper;
	
	@Override
	@Transactional
	public void saveMain(RequestOrder requestOrder, List<ResponseOrder> responseOrderList) {
		requestOrderMapper.insert(requestOrder);
		if(responseOrderList!=null && responseOrderList.size()>0) {
			for(ResponseOrder entity:responseOrderList) {
				//外键设置
				entity.setRequestId(requestOrder.getId());
				responseOrderMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(RequestOrder requestOrder,List<ResponseOrder> responseOrderList) {
		requestOrderMapper.updateById(requestOrder);
		
		//1.先删除子表数据
		responseOrderMapper.deleteByMainId(requestOrder.getId());
		
		//2.子表数据重新插入
		if(responseOrderList!=null && responseOrderList.size()>0) {
			for(ResponseOrder entity:responseOrderList) {
				//外键设置
				entity.setRequestId(requestOrder.getId());
				responseOrderMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		responseOrderMapper.deleteByMainId(id);
		requestOrderMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			responseOrderMapper.deleteByMainId(id.toString());
			requestOrderMapper.deleteById(id);
		}
	}
	
}
