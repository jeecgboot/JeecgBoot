package com.shop.common.util;

import com.shop.dto.HotProductDTO;
import com.shop.dto.ProductDTO;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ProductUtil {

    public static List<HotProductDTO> getHotProductList(List<ProductDTO> productsList) {
        return productsList.stream()
                .sorted(Comparator.comparingInt(productDTO -> (int) -(productDTO.getSellCardMember())))
                .limit(6)
                .map(productDTO -> doToHotProductDTO(productDTO))
                .collect(Collectors.toList());
    }

    public static HotProductDTO doToHotProductDTO(ProductDTO productDTO) {
        HotProductDTO hotProductDTO = new HotProductDTO();
        hotProductDTO.setId(productDTO.getId());
        hotProductDTO.setName(productDTO.getName());
        hotProductDTO.setPrice(productDTO.getPrice());
        hotProductDTO.setImage(productDTO.getImageLogo());
        hotProductDTO.setLink(productDTO.getLink());
        return hotProductDTO;
    }

}
