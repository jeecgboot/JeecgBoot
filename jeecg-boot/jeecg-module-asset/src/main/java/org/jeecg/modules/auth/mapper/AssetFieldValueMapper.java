package org.jeecg.modules.auth.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.auth.entity.AssetFieldValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Collection;
import java.util.List;

/**
 * 资产字段值Mapper
 * 
 * @author: local.clk
 * @date: 2026-02-09
 */
public interface AssetFieldValueMapper extends BaseMapper<AssetFieldValue> {
    List<AssetFieldValue> getAssetFieldValueList(@Param("tenantId") int tenantId, @Param("assetIds")Collection<Long> assetIds, @Param("fieldDefinitionIds")Collection<Long> fieldDefinitionIds);
}
