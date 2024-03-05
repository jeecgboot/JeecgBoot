package org.jeecg.modules.business.domain.api.shouman;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.jeecg.modules.business.entity.Country;
import org.jeecg.modules.business.entity.ShoumanOrderContent;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Data
public class OrderCreationRequestBody implements RequestBody {

    private List<ShoumanOrderContent> orderContents;

    private Map<String, Country> countryMap;

    private final static String DEFAULT_SPLIT = ";";
    private final static String LINE_BREAK = "\n";
    private final static String CUSTOM = "定制";
    private final static String QUOTE = ":";
    private final static String WIA = "维亚智通";
    private final static String TRANSACTION_NUMBER = "交易号";

    public OrderCreationRequestBody(List<ShoumanOrderContent> orderContents, Map<String, Country> countryMap) {
        this.orderContents = orderContents;
        this.countryMap = countryMap;
    }

    @Override
    public String path() {
        return "/order/openapi/orders/add";
    }

    @Override
    public JSONObject parameters() {
        JSONObject json = new JSONObject();
        // TODO: 2023/11/29 Change to real address
        ShoumanOrderContent anyContent = orderContents.get(0);
        putNonNull(json, "address", "收货人：王生 收货地址： 广东省东镇芦莞市寮步溪二路40号汇元佳科技园二栋10楼公司前台 手机：17633551138");
        putNonNull(json, "addressee", "王生");
        putNonNull(json, "city", "东莞市");
        String countryName = anyContent.getCountry();
        Country country = countryMap.get(countryName);
        putNonNull(json, "country", "CHINA");
        putNonNull(json, "countryCode", "CN");
        // TODO: 2023/11/29 Change to real address
        putNonNull(json, "orderId", anyContent.getPlatformOrderId());
        JSONArray outboundInfos = new JSONArray();
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (ShoumanOrderContent content : orderContents) {
            JSONObject contentJson = new JSONObject();
            putNonNull(contentJson, "productName", content.getProductName());
            putNonNull(contentJson, "customerId", content.getPlatformOrderNumber());
            BigDecimal price = content.getPrice();
            putNonNull(contentJson, "price", price.toString());
            totalPrice = totalPrice.add(price);
            putNonNull(contentJson, "theImagePath", content.getImageUrl());
            putNonNull(contentJson, "comment", generateRemark(content.getRemark(), content.getCustomizationData(),
                    content.getContentRecRegex(), content.getContentExtRegex(), content.getPlatformOrderNumber()));
            putNonNull(contentJson, "sku", content.getSku());
            putNonNull(contentJson, "outboundNumder", content.getQuantity()); // Typo intended
            outboundInfos.add(contentJson);
        }
        putNonNull(json, "totalPrice", totalPrice.toString());
        putNonNull(json, "outboundInfos", outboundInfos);
        return json;
    }

    private String generateRemark(String baseRemark, String customizationData, String contentRecRegex, String contentExtRegex,
                                  String platformOrderNumber) {
        StringBuilder sb = new StringBuilder();
        String[] baseRemarks = baseRemark.split(DEFAULT_SPLIT);
        for (String remark : baseRemarks) {
            sb.append(remark)
                    .append(LINE_BREAK);
        }

        String[] strings = customizationData.split(DEFAULT_SPLIT);
        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];
            if (string.matches(contentRecRegex)) {
                String trimmed = string.trim();
                String content = trimmed.split(contentExtRegex)[1];
                sb.append(CUSTOM)
                        .append(i + 1)
                        .append(QUOTE)
                        .append(content)
                        .append(LINE_BREAK);
            }
        }
        sb.append(TRANSACTION_NUMBER)
                .append(platformOrderNumber)
                .append(LINE_BREAK);
        Calendar instance = Calendar.getInstance();
        // Add date (format MM-dd) and company name at the end
        sb.append(WIA)
                .append("(")
                .append(instance.get(Calendar.MONTH) + 1) // Starts with 0, so must add 1
                .append("-")
                .append(instance.get(Calendar.DAY_OF_MONTH))
                .append(")");
        return sb.toString();
    }

    private <E> void putNonNull(JSONObject json, String key, E value) {
        if (value != null) {
            json.put(key, value);
        }
    }
}
