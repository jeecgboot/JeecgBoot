package org.jeecg.modules.asset.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jeecg.modules.auth.vo.AssetFieldValueVo;

import java.util.List;

/**
 * Project：jeecg-boot
 * Created by：lc5198
 * Date：2026/2/9 10:57
 */
@Data
@Schema(description = "获取资产分页参数")
@NoArgsConstructor
@AllArgsConstructor
public class QueryPageListDto {
    private Integer pageNo = 1;
    private Integer pageSize = 20;

    private List<AssetFieldValueVo> options;

    public QueryPageListDto(Integer pageNo, Integer pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }
}
