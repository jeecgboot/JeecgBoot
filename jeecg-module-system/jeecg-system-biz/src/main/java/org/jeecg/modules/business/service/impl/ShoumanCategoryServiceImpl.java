package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.entity.Shouman.ShoumanCategory;
import org.jeecg.modules.business.entity.Shouman.ShoumanSkuRelation;
import org.jeecg.modules.business.mapper.ShoumanCategoryMapper;
import org.jeecg.modules.business.mapper.ShoumanSkuRelationMapper;
import org.jeecg.modules.business.service.IShoumanCategoryService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 首曼产品类别
 * @Author: jeecg-boot
 * @Date:   2023-11-29
 * @Version: V1.0
 */
@Service
public class ShoumanCategoryServiceImpl extends ServiceImpl<ShoumanCategoryMapper, ShoumanCategory> implements IShoumanCategoryService {

	@Autowired
	private ShoumanCategoryMapper shoumanCategoryMapper;
	@Autowired
	private ShoumanSkuRelationMapper shoumanSkuRelationMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(ShoumanCategory shoumanCategory, List<ShoumanSkuRelation> shoumanSkuRelationList) {
		shoumanCategoryMapper.insert(shoumanCategory);
		if(shoumanSkuRelationList!=null && shoumanSkuRelationList.size()>0) {
			for(ShoumanSkuRelation entity:shoumanSkuRelationList) {
				//外键设置
				entity.setShoumanCategoryId(shoumanCategory.getId());
				shoumanSkuRelationMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(ShoumanCategory shoumanCategory,List<ShoumanSkuRelation> shoumanSkuRelationList) {
		shoumanCategoryMapper.updateById(shoumanCategory);
		
		//1.先删除子表数据
		shoumanSkuRelationMapper.deleteByMainId(shoumanCategory.getId());
		
		//2.子表数据重新插入
		if(shoumanSkuRelationList!=null && !shoumanSkuRelationList.isEmpty()) {
			for(ShoumanSkuRelation entity:shoumanSkuRelationList) {
				//外键设置
				entity.setShoumanCategoryId(shoumanCategory.getId());
				shoumanSkuRelationMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		shoumanSkuRelationMapper.deleteByMainId(id);
		shoumanCategoryMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			shoumanSkuRelationMapper.deleteByMainId(id.toString());
			shoumanCategoryMapper.deleteById(id);
		}
	}
	
}
