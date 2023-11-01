package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * 自定义基础实体类
 */
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class BaseModel<T extends Model<?>> extends Model<T> {
    //排序字段
    @TableField(exist = false)
    @JsonIgnore
    private String column = "id";
    //排序方式
    @TableField(exist = false)
    @JsonIgnore
    private String order;//ascend,descend
    //时间
    @TableField(exist = false)
    @JsonIgnore
    private String ts_create;

    public String getOrder() {
        return "ascend".equals(order)?"asc":"desc";
    }
    public String getColumn() {
        if(StringUtils.isEmpty(order)&&!"id".equals(column)){
            column = "id";
        }
        return column;
    }
}
