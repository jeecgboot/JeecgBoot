package org.jeecg.modules.business.domain.api.mabang.stockDoChangeStock;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.jeecg.modules.business.domain.api.mabang.RequestBody;
import org.jeecg.modules.business.domain.api.mabang.doSearchSkuListNew.SkuData;

import java.math.BigDecimal;
import java.util.function.Function;

@Getter
@Setter
public class SkuChangeRequestBody implements RequestBody {

    private String stockSku;
    private String nameCn;
    private String nameEn;
    private Integer status;
    private BigDecimal salePrice;
    private String declareName;
    private String declareEname;
    private String warehouse;
    private String remark;
    private Integer weight;
    private Integer isGift;

    @Override
    public String api() {
        return "stock-do-change-stock";
    }

    @Override
    public JSONObject parameters() {
        JSONObject json = new JSONObject();
        putNonNull(json, "stockSku", stockSku);
        putNonNull(json, "nameCN", nameCn);
        putNonNull(json, "nameEN", nameEn);
        putNonNull(json, "status", status);
        putNonNull(json, "salePrice", salePrice);
        putNonNull(json, "declareName", declareName);
        putNonNull(json, "declareEname", declareEname);
        putNonNull(json, "weight", weight);
        putNonNull(json, "saleRemark", remark);
        putNonNull(json, "isGift", isGift);
        return json;
    }

    public SkuChangeRequestBody(SkuData data) {
        this.stockSku = data.getErpCode();
        this.nameCn = data.getNameCN();
        this.nameEn = data.getNameEN();
        this.salePrice = data.getSalePrice();
        this.declareName = data.getDeclareNameZh();
        this.declareEname = data.getDeclareNameEn();
        this.remark = data.getSaleRemark();
        this.weight = data.getWeight();
        this.isGift = data.getIsGift();
    }

    private <E> void putNonNull(JSONObject json, String key, E value) {
        if (value != null) {
            json.put(key, value);
        }
    }
    private <E, T> void putNonNull(JSONObject json, String key, E value, Function<E, T> mapper) {
        if (value != null) {
            json.put(key, mapper.apply(value));
        }
    }
}
