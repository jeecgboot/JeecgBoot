package org.jeecg.modules.auth.service.impl;

import org.jeecg.modules.auth.entity.AssetFieldValue;
import org.jeecg.modules.auth.mapper.AssetFieldValueMapper;
import org.jeecg.modules.auth.service.IAssetFieldValueService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;

/**
 * 资产字段值服务实现
 *
 * @author: local.clk
 * @date: 2026-02-09
 */
@Service
@Slf4j
public class AssetFieldValueServcieImpl extends ServiceImpl<AssetFieldValueMapper, AssetFieldValue>
        implements IAssetFieldValueService {

    @Override
    public List<AssetFieldValue> getAssetFieldValueList(int tenantId, Collection<Long> assetIds, Collection<Long> fieldDefinitionIds) {
        return baseMapper.getAssetFieldValueList(tenantId, assetIds, fieldDefinitionIds);
    }
}
