package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.entity.Shouman.ShoumanSkuRelation;
import org.jeecg.modules.business.mapper.ShoumanSkuRelationMapper;
import org.jeecg.modules.business.service.IShoumanSkuRelationService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 首曼产品类别与SKU关系
 * @Author: jeecg-boot
 * @Date:   2023-11-29
 * @Version: V1.0
 */
@Service
public class ShoumanSkuRelationServiceImpl extends ServiceImpl<ShoumanSkuRelationMapper, ShoumanSkuRelation> implements IShoumanSkuRelationService {
	
	@Autowired
	private ShoumanSkuRelationMapper shoumanSkuRelationMapper;
	
	@Override
	public List<ShoumanSkuRelation> selectByMainId(String mainId) {
		return shoumanSkuRelationMapper.selectByMainId(mainId);
	}
}
