package com.bomaos.common.core.pays.epusdt.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class EpusdtEntity {
    private Integer status_code;
    private String message;
    private EpusdtData data;
    private String request_id;
}
