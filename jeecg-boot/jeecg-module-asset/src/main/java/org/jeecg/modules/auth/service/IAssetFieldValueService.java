package org.jeecg.modules.auth.service;

import org.jeecg.modules.auth.entity.AssetFieldValue;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;

/**
 * 资产字段值服务接口
 * 
 * @author: local.clk
 * @date: 2026-02-09
 */
public interface IAssetFieldValueService extends IService<AssetFieldValue> {
    List<AssetFieldValue> getAssetFieldValueList(int tenantId, Collection<Long> assetIds,Collection<Long> fieldDefinitionIds);
}