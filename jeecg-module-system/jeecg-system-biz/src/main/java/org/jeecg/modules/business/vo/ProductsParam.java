package org.jeecg.modules.business.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ProductsParam {
    private final List<String> ids;
    private final int weight;
    public ProductsParam(@JsonProperty("ids") List<String> ids, @JsonProperty("weight") int weight){
        this.ids = ids;
        this.weight = weight;
    }
    public List<String> getIds() { return ids; }
    public int getWeight() { return  weight; }
}
