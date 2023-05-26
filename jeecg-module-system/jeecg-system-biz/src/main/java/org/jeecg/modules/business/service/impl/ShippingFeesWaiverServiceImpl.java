package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.entity.ShippingFeesWaiver;
import org.jeecg.modules.business.entity.ShippingFeesWaiverProduct;
import org.jeecg.modules.business.mapper.ShippingFeesWaiverProductMapper;
import org.jeecg.modules.business.mapper.ShippingFeesWaiverMapper;
import org.jeecg.modules.business.service.IShippingFeesWaiverService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 采购运费免除
 * @Author: jeecg-boot
 * @Date:   2021-06-02
 * @Version: V1.0
 */
@Service
public class ShippingFeesWaiverServiceImpl extends ServiceImpl<ShippingFeesWaiverMapper, ShippingFeesWaiver> implements IShippingFeesWaiverService {

	@Autowired
	private ShippingFeesWaiverMapper shippingFeesWaiverMapper;
	@Autowired
	private ShippingFeesWaiverProductMapper shippingFeesWaiverProductMapper;
	
	@Override
	@Transactional
	public void saveMain(ShippingFeesWaiver shippingFeesWaiver, List<ShippingFeesWaiverProduct> shippingFeesWaiverProductList) {
		shippingFeesWaiverMapper.insert(shippingFeesWaiver);
		if(shippingFeesWaiverProductList!=null && shippingFeesWaiverProductList.size()>0) {
			for(ShippingFeesWaiverProduct entity:shippingFeesWaiverProductList) {
				//外键设置
				entity.setWaiverId(shippingFeesWaiver.getId());
				shippingFeesWaiverProductMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(ShippingFeesWaiver shippingFeesWaiver,List<ShippingFeesWaiverProduct> shippingFeesWaiverProductList) {
		shippingFeesWaiverMapper.updateById(shippingFeesWaiver);
		
		//1.先删除子表数据
		shippingFeesWaiverProductMapper.deleteByMainId(shippingFeesWaiver.getId());
		
		//2.子表数据重新插入
		if(shippingFeesWaiverProductList!=null && shippingFeesWaiverProductList.size()>0) {
			for(ShippingFeesWaiverProduct entity:shippingFeesWaiverProductList) {
				//外键设置
				entity.setWaiverId(shippingFeesWaiver.getId());
				shippingFeesWaiverProductMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		shippingFeesWaiverProductMapper.deleteByMainId(id);
		shippingFeesWaiverMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			shippingFeesWaiverProductMapper.deleteByMainId(id.toString());
			shippingFeesWaiverMapper.deleteById(id);
		}
	}
	
}
