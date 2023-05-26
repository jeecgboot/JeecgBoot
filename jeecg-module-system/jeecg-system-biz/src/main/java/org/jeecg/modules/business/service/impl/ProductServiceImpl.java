package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.entity.Product;
import org.jeecg.modules.business.entity.Sku;
import org.jeecg.modules.business.mapper.SkuMapper;
import org.jeecg.modules.business.mapper.ProductMapper;
import org.jeecg.modules.business.service.IProductService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 商品
 * @Author: jeecg-boot
 * @Date:   2021-04-01
 * @Version: V1.0
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private SkuMapper skuMapper;
	
	@Override
	@Transactional
	public void saveMain(Product product, List<Sku> skuList) {
		productMapper.insert(product);
		if(skuList!=null && skuList.size()>0) {
			for(Sku entity:skuList) {
				//外键设置
				entity.setProductId(product.getId());
				skuMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(Product product,List<Sku> skuList) {
		productMapper.updateById(product);
		
		//1.先删除子表数据
		skuMapper.deleteByMainId(product.getId());
		
		//2.子表数据重新插入
		if(skuList!=null && skuList.size()>0) {
			for(Sku entity:skuList) {
				//外键设置
				entity.setProductId(product.getId());
				skuMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		skuMapper.deleteByMainId(id);
		productMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			skuMapper.deleteByMainId(id.toString());
			productMapper.deleteById(id);
		}
	}
	
}
