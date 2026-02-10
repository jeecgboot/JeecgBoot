package org.jeecg.modules.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.jeecg.modules.auth.model.FieldPermissionModel;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * Project：jeecg-boot
 * Created by：lc5198
 * Date：2026/2/10 15:09
 */
@Data
@Schema(description = "配置对象")
public class SetRoleFieldPermissionDTO {
    @NotBlank(message = "Role Id can not be null")
    @Schema(description = "Role ID")
    private String roleId;

    @Schema(description = "Field Permission List")
    private List<FieldPermissionModel> fieldPermissionList;

    public List<FieldPermissionModel> getFieldPermissionList() {
        if (CollectionUtils.isEmpty(fieldPermissionList)) {
            return Collections.emptyList();
        }
        return fieldPermissionList;
    }
}
