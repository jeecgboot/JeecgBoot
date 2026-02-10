package org.jeecg.modules.auth.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.jeecg.modules.auth.config.PermissionAction;

import java.util.List;

/**
 * Project：jeecg-boot
 * Created by：lc5198
 * Date：2026/2/10 15:11
 */
@Data
@Schema(description = "FieldPermissionModel")
public class FieldPermissionModel {
    @Schema(example = "1086")
    private Long fieldDefinitionId;
    @Schema(example = "[\"CREATE\",\"READ\",\"UPDATE\",\"DELETE\"]")
    private List<PermissionAction> permissionActions;
}
