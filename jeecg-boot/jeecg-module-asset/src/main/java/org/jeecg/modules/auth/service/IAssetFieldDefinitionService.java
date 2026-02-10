package org.jeecg.modules.auth.service;

import org.jeecg.modules.auth.entity.AssetFieldDefinition;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 资产字段定义服务接口
 * 
 * @author: local.clk
 * @date: 2026-02-09
 */
public interface IAssetFieldDefinitionService extends IService<AssetFieldDefinition> {
    String generateFieldKey(int tenantId);

    boolean fieldKeyExist(String fieldKey);
}
