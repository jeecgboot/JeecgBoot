package org.jeecg.modules.asset.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.asset.dto.AssetFiledConvertDto;
import org.jeecg.modules.asset.entity.Asset;
import org.jeecg.modules.asset.vo.AssetVo;
import org.jeecg.modules.auth.config.FieldCategoryEnum;
import org.jeecg.modules.auth.dto.RoleFieldPermissionsDTO;
import org.jeecg.modules.auth.entity.AssetFieldDefinition;
import org.jeecg.modules.auth.entity.AssetFieldValue;
import org.jeecg.modules.auth.service.IAssetFieldRolePermissionService;
import org.jeecg.modules.auth.service.IAssetFieldValueService;
import org.jeecg.modules.auth.vo.AssetFieldValueVo;
import org.jeecg.modules.tenant.service.TenantService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Project：jeecg-boot
 * Created by：lc5198
 * Date：2026/2/10 13:45
 */
@Component
@Slf4j
public class AssetFieldParserUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final TenantService tenantService;

    private final IAssetFieldRolePermissionService assetFieldRolePermissionService;

    private final IAssetFieldValueService assetFieldValueService;

    public AssetFieldParserUtil(TenantService tenantService,
                                IAssetFieldRolePermissionService assetFieldRolePermissionService,
                                IAssetFieldValueService assetFieldValueService) {
        this.tenantService = tenantService;
        this.assetFieldRolePermissionService = assetFieldRolePermissionService;
        this.assetFieldValueService = assetFieldValueService;
    }

    private static final AssetFiledConvertDto EMPTY_CONVERT_DTO = AssetFiledConvertDto.builder()
            .readFieldMap(Collections.emptyMap())
            .valuesByAssetId(Collections.emptyMap())
            .build();

    public AssetFiledConvertDto getAssetFiledConvertDto(String userId, int tenantId, List<Long> assetIds) {

        if (StringUtils.isBlank(userId) || CollectionUtils.isEmpty(assetIds)) {
            return EMPTY_CONVERT_DTO;
        }

        List<String> roles = tenantService.getRoleIdByUserId(userId, tenantId);
        if (CollectionUtils.isEmpty(roles)) {
            return EMPTY_CONVERT_DTO;
        }

        RoleFieldPermissionsDTO dto = assetFieldRolePermissionService.getRoleFieldPermissions(roles, FieldCategoryEnum.Asset);
        if (dto == null || CollectionUtils.isEmpty(dto.getReadFields())) {
            return EMPTY_CONVERT_DTO;
        }

        List<Long> readFieldDefinitionIds = dto.getReadFields().stream()
                .map(AssetFieldDefinition::getId)
                .toList();
        Map<Long, AssetFieldDefinition> readFieldMap = dto.getReadFields().stream()
                .sorted(Comparator.comparing(AssetFieldDefinition::getSort,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toMap(
                        AssetFieldDefinition::getId, e -> e, (a, b) -> a, LinkedHashMap::new));

        // 按资产ID查询字段值并分组，使每个资产只使用自己的字段值
        Map<Long, List<AssetFieldValue>> valuesByAssetId = Collections.emptyMap();
        if (!readFieldDefinitionIds.isEmpty()) {
            List<AssetFieldValue> assetFieldValues = assetFieldValueService.getAssetFieldValueList(
                    tenantId,
                    assetIds,
                    readFieldDefinitionIds);
            if (!CollectionUtils.isEmpty(assetFieldValues)) {
                valuesByAssetId = assetFieldValues.stream()
                        .collect(Collectors.groupingBy(AssetFieldValue::getAssetId));
            }
        }

        return AssetFiledConvertDto.builder().valuesByAssetId(valuesByAssetId).readFieldMap(readFieldMap).build();
    }

    public AssetVo parse(Asset asset, AssetFiledConvertDto assetFiledConvertDto) {
        Objects.requireNonNull(asset, "asset must not be null");
        Objects.requireNonNull(assetFiledConvertDto, "assetFiledConvertDto must not be null");

        AssetVo assetVo = new AssetVo();
        BeanUtils.copyProperties(asset, assetVo);

        Map<Long, List<AssetFieldValue>> valuesByAssetId = assetFiledConvertDto.getValuesByAssetId();
        Map<Long, AssetFieldDefinition> readFieldMap = assetFiledConvertDto.getReadFieldMap();
        if (valuesByAssetId == null || readFieldMap == null || asset.getId() == null) {
            return assetVo;
        }

        List<AssetFieldValue> thisAssetValues = valuesByAssetId.getOrDefault(asset.getId(), Collections.emptyList());
        List<AssetFieldValueVo> fieldValueVos = buildFieldValueVos(thisAssetValues, readFieldMap);
        assetVo.setAssetFieldValues(fieldValueVos);
        assetVo.setValueMap(buildValueMap(readFieldMap, fieldValueVos));
        return assetVo;
    }

    private List<AssetFieldValueVo> buildFieldValueVos(List<AssetFieldValue> values,
                                                       Map<Long, AssetFieldDefinition> readFieldMap) {
        return values.stream()
                .map(value -> {
                    AssetFieldDefinition definition = readFieldMap.get(value.getFieldDefinitionId());
                    if (definition == null) {
                        return null;
                    }
                    AssetFieldValueVo vo = new AssetFieldValueVo();
                    vo.setFieldKey(definition.getFieldKey());
                    vo.setSort(definition.getSort() != null ? definition.getSort() : 0);
                    setFieldValueByType(vo, value, definition.getFieldType());
                    return vo;
                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(AssetFieldValueVo::getSort,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
    }

    private void setFieldValueByType(AssetFieldValueVo vo, AssetFieldValue value,
                                     org.jeecg.modules.auth.config.FieldTypeEnum fieldType) {
        if (fieldType == null) {
            vo.setFieldValue(value.getValueText());
            return;
        }
        switch (fieldType) {
            case Number -> vo.setFieldValue(value.getValueNumber());
            case Date -> vo.setFieldValue(parseDate(value.getValueDate(), DATE_FORMATTER));
            case Time -> vo.setFieldValue(parseDate(value.getValueTime(), TIME_FORMATTER));
            case Date_Time -> vo.setFieldValue(parseDate(value.getValueDatetime(), DATE_TIME_FORMATTER));
            case Boolean -> vo.setFieldValue(value.getValueBoolean());
            case Json -> vo.setFieldValue(parseJson(value.getValueJson()));
            default -> vo.setFieldValue(value.getValueText());
        }
    }

    private Map<String, Object> buildValueMap(Map<Long, AssetFieldDefinition> readFieldMap,
                                              List<AssetFieldValueVo> fieldValueVos) {
        Map<String, Object> valueMap = new LinkedHashMap<>();
        for (Map.Entry<Long, AssetFieldDefinition> entry : readFieldMap.entrySet()) {
            String fieldKey = entry.getValue().getFieldKey();
            if (StringUtils.isNotBlank(fieldKey)) {
                valueMap.put(fieldKey, null);
            }
        }
        for (AssetFieldValueVo vo : fieldValueVos) {
            if (StringUtils.isNotBlank(vo.getFieldKey())) {
                valueMap.put(vo.getFieldKey(), vo.getFieldValue());
            }
        }
        return valueMap;
    }

    private JsonNode parseJson(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readTree(text);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse JSON, text: {}", text, e);
            return null;
        }
    }

    private String parseDate(Date date, DateTimeFormatter dateTimeFormatter) {
        if (date == null) {
            return null;
        }

        try {
            return date.toInstant().atZone(ZoneId.systemDefault()).format(dateTimeFormatter);
        } catch (Exception e) {
            log.error("Failed to format date, value: {}", date, e);
            return null;
        }
    }
}
