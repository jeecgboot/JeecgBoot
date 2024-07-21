package com.bomaos.reception.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString
@Data
public class ProductListDTO {

    /**
     * id
     */
    private Integer id;

    /**
     * 分类标题
     */
    private String title;

    /**
     * 商品数量
     */
    private Long productNum;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String UpdateTime;

    /**
     * 商品列表
     */
    private List<ProductDTO> productDTOList;
}
