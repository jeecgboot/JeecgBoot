package com.vmq.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("公共返回VO")
public class CommonRes<V> {

    @ApiModelProperty("状态码：-1失败，1成功")
    private int code;

    @ApiModelProperty("提示内容")
    private String msg;

    @ApiModelProperty("数据")
    private V data;

}
