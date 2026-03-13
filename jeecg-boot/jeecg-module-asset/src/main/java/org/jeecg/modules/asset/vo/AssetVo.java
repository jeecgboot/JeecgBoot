package org.jeecg.modules.asset.vo;

import org.jeecg.modules.asset.entity.Asset;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.modules.auth.vo.AssetFieldValueVo;

@Data
@EqualsAndHashCode(callSuper = true)
public class AssetVo extends Asset {
    private List<AssetFieldValueVo> assetFieldValues;
    private Map<String, Object> valueMap;
}
