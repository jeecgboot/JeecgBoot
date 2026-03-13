package org.jeecg.modules.asset.dto;

import lombok.Builder;
import lombok.Data;
import org.jeecg.modules.auth.entity.AssetFieldDefinition;
import org.jeecg.modules.auth.entity.AssetFieldValue;

import java.util.List;
import java.util.Map;

/**
 * Project：jeecg-boot
 * Created by：lc5198
 * Date：2026/2/10 13:47
 */
@Data
@Builder
public class AssetFiledConvertDto {
    private Map<Long, AssetFieldDefinition> readFieldMap;
    private Map<Long, List<AssetFieldValue>> valuesByAssetId;
}
