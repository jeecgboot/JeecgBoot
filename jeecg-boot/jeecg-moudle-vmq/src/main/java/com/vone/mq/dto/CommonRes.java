package com.vone.mq.dto;

import lombok.Data;

@Data
public class CommonRes<V> {

    private int code;
    private String msg;
    private V data;

}
