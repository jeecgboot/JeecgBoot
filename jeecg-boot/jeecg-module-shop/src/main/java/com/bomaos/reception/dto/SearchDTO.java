package com.bomaos.reception.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SearchDTO {

    private Integer id;
    private Integer andIncrement;
    private String member;
    private String createTime;
    private String payType;
    private String status;
    private String money;

}
