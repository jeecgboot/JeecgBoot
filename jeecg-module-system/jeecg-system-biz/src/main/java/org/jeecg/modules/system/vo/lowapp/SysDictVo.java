package org.jeecg.modules.system.vo.lowapp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.entity.SysDictItem;

import java.util.List;

@Data
public class SysDictVo {
    /**
     * 字典id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典编码
     */
    private String dictCode;

    /**
     * 应用id
     */
    private String lowAppId;
    
    /**
     * 租户ID
     */
    private Integer tenantId;

    /**
     * 字典子项
     */
    private List<SysDictItem> dictItemsList;
}
