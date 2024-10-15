package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.entity.Shouman.ShoumanSkuRelation;

import java.util.List;

/**
 * @Description: 首曼产品类别与SKU关系
 * @Author: jeecg-boot
 * @Date:   2023-11-29
 * @Version: V1.0
 */
public interface IShoumanSkuRelationService extends IService<ShoumanSkuRelation> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<ShoumanSkuRelation>
	 */
	public List<ShoumanSkuRelation> selectByMainId(String mainId);
}
