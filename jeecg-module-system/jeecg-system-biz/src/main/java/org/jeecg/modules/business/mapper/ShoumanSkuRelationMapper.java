package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.Shouman.ShoumanSkuRelation;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 首曼产品类别与SKU关系
 * @Author: jeecg-boot
 * @Date:   2023-11-29
 * @Version: V1.0
 */
@Repository
public interface ShoumanSkuRelationMapper extends BaseMapper<ShoumanSkuRelation> {

	/**
	 * 通过主表id删除子表数据
	 *
	 * @param mainId 主表id
	 * @return boolean
	 */
	public boolean deleteByMainId(@Param("mainId") String mainId);

  /**
   * 通过主表id查询子表数据
   *
   * @param mainId 主表id
   * @return List<ShoumanSkuRelation>
   */
	public List<ShoumanSkuRelation> selectByMainId(@Param("mainId") String mainId);
}
