package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.entity.Shouman.ShoumanCategory;
import org.jeecg.modules.business.entity.Shouman.ShoumanSkuRelation;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 首曼产品类别
 * @Author: jeecg-boot
 * @Date:   2023-11-29
 * @Version: V1.0
 */
public interface IShoumanCategoryService extends IService<ShoumanCategory> {

	/**
	 * 添加一对多
	 *
	 * @param shoumanCategory
	 * @param shoumanSkuRelationList
	 */
	public void saveMain(ShoumanCategory shoumanCategory,List<ShoumanSkuRelation> shoumanSkuRelationList) ;
	
	/**
	 * 修改一对多
	 *
   * @param shoumanCategory
   * @param shoumanSkuRelationList
	 */
	public void updateMain(ShoumanCategory shoumanCategory,List<ShoumanSkuRelation> shoumanSkuRelationList);
	
	/**
	 * 删除一对多
	 *
	 * @param id
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 *
	 * @param idList
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
