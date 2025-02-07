package org.jeecg.modules.business.domain.api.mabang.stockDoAddStock;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.jeecg.modules.business.domain.api.mabang.RequestBody;
import org.jeecg.modules.business.domain.api.mabang.doSearchSkuListNew.Label;
import org.jeecg.modules.business.domain.api.mabang.doSearchSkuListNew.SkuData;

import java.math.BigDecimal;
import java.util.function.Function;

@Getter
@Setter
public class SkuAddRequestBody implements RequestBody {

    private String stockSku;
    private String nameCn;
    private String nameEn;
    private Integer status = 3;
    private BigDecimal salePrice;
    private BigDecimal declareValue;
    private String declareName;
    private String declareEname;
    private String warehouse;
    private String remark;
    private Label[] labelData;
    private Integer hasBattery;
    private Integer magnetic;
    private Integer powder;
    private Integer isPaste;
    private Integer noLiquidCosmetic;
    private Integer isFlammable;
    private Integer isKnife;
    private Integer isGift;
    private String supplier;
    private String supplierLink;
    private String picture;

    @Override
    public String api() {
        return "stock-do-add-stock";
    }

    @Override
    public JSONObject parameters() {
        JSONObject json = new JSONObject();
        putNonNull(json, "stockSku", stockSku);
        putNonNull(json, "nameCN", nameCn);
        putNonNull(json, "nameEN", nameEn);
        putNonNull(json, "status", status);
        putNonNull(json, "salePrice", salePrice);
        putNonNull(json, "declareValue", declareValue);
        putNonNull(json, "declareName", declareName);
        putNonNull(json, "declareEname", declareEname);
        if(labelData != null) {
            JSONArray labelDataArray = new JSONArray();
            for(Label label : labelData) {
                JSONObject labelJson = new JSONObject();
                labelJson.put("name", label.getName());
                labelDataArray.add(labelJson);
            }
            json.put("labelData", labelDataArray.toJSONString());
        }
        JSONArray warehouseData = new JSONArray();
        JSONObject warehouse = new JSONObject();
        warehouse.put("name", this.warehouse);
        warehouseData.add(warehouse);
        json.put("warehouseData", warehouseData.toJSONString());
        putNonNull(json, "weight", remark);
        putNonNull(json, "saleRemark", remark);
        putNonNull(json, "hasBattery", hasBattery);
        putNonNull(json, "magnetic", magnetic);
        putNonNull(json, "powder", powder);
        putNonNull(json, "ispaste", isPaste);
        putNonNull(json, "noLiquidCosmetic", noLiquidCosmetic);
        putNonNull(json, "is_flammable", isFlammable);
        putNonNull(json, "is_knife", isKnife);
        putNonNull(json, "isGift", isGift);
        putNonNull(json, "autoCreateSupplier", 1);
        JSONArray supplierData = new JSONArray();
        JSONObject supplier = new JSONObject();
        supplier.put("name", this.supplier);
        supplier.put("productLinkAddress", this.supplierLink);
        supplier.put("flag", 1);
        supplierData.add(supplier);
        json.put("suppliersData", supplierData.toJSONString());
        putNonNull(json, "picture", picture);
        return json;
    }

    public SkuAddRequestBody(SkuData data) {
        this.stockSku = data.getErpCode();
        this.nameCn = data.getNameCN();
        this.nameEn = data.getNameEN();
        this.salePrice = data.getSalePrice();
        this.declareValue = data.getDeclareValue();
        this.declareName = data.getDeclareNameZh();
        this.declareEname = data.getDeclareNameEn();
        this.warehouse = data.getWarehouseName();
        this.remark = data.getSaleRemark();
        this.labelData = data.getLabelData();
        this.hasBattery = data.getHasBattery();
        this.magnetic = data.getMagnetic();
        this.powder = data.getPowder();
        this.isPaste = data.getIsPaste();
        this.noLiquidCosmetic = data.getNoLiquidCosmetic();
        this.isFlammable = data.getIsFlammable();
        this.isKnife = data.getIsKnife();
        this.isGift = data.getIsGift();
        this.supplier = data.getSupplier();
        this.supplierLink = data.getSupplierLink();
        this.picture = data.getSalePicture();
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
