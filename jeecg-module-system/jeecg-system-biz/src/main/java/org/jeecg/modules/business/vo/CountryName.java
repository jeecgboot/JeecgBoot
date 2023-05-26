package org.jeecg.modules.business.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@TableName("channel_price_country")
public class CountryName {

    @JsonProperty("code")
    @TableField("effective_country")
    private final String code;

    @JsonProperty("nameZh")
    @TableField("name_zh")
    private final String zh_name;

    @JsonProperty("nameEn")
    @TableField("name_en")
    private final String en_name;
}
