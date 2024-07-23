package com.shop.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class HotProductDTO {
    private Integer id;
    private String name;
    private String image;
    private String price;
    private String link;
}
