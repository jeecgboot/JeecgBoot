package org.jeecg.modules.business.vo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("popular_country")
public class PopularCountry {
    @TableField("country")
    private final String name_en;

    @TableField("name_zh")
    private final String name_zh;

    @TableField("code")
    private final String code;

    @TableField("quantity")
    private final Long quantity;

}
