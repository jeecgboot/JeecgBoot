package org.jeecg.modules.auth.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.jeecg.modules.auth.entity.AssetFieldDefinition;
import org.jeecg.modules.auth.mapper.AssetFieldDefinitionMapper;
import org.jeecg.modules.auth.service.IAssetFieldDefinitionService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;

/**
 * 资产字段定义服务实现
 *
 * @author: local.clk
 * @date: 2026-02-09
 */
@Service
@Slf4j
public class AssetFieldDefinitionServiceImpl extends ServiceImpl<AssetFieldDefinitionMapper, AssetFieldDefinition>
        implements IAssetFieldDefinitionService {

    private static final char[] ALPHANUMERICAL_CHARS = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1',
            '2', '3', '4', '5', '6', '7', '8', '9'};


    @Override
    public String generateFieldKey(int tenantId) {
        String key;

        while (true) {
            key = tenantId + "_" + RandomStringUtils.secure().next(5, ALPHANUMERICAL_CHARS);

            if (lambdaQuery().eq(AssetFieldDefinition::getFieldKey, key).count() == 0) {
                log.info("Key:{} 在当前租户:{} 中不存在", key, tenantId);
                break;
            }

            log.warn("Key:{} 在当前租户:{} 中存在;继续获取一个", key, tenantId);
        }
        return key;
    }

    @Override
    public boolean fieldKeyExist(String fieldKey) {
        return lambdaQuery().eq(AssetFieldDefinition::getFieldKey, fieldKey).count() > 0;
    }
}
