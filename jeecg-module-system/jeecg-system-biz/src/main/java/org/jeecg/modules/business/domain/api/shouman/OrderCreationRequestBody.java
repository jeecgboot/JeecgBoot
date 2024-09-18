package org.jeecg.modules.business.domain.api.shouman;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.jeecg.modules.business.entity.Country;
import org.jeecg.modules.business.entity.ShoumanOrderContent;
import org.jeecg.modules.business.entity.ShoumanRegex;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class OrderCreationRequestBody implements RequestBody {

    private List<ShoumanOrderContent> orderContents;

    private final static String DEFAULT_SPLIT = ";";
    private final static String LINE_BREAK = "\n";
    private final static String QUOTE = ":";
    private final static String WIA = "维亚智通";
    private final static String TRANSACTION_NUMBER = "交易号";
    private final static String SHOP_CODE = "店铺名称";
    private final static String PRODUCT_GROUP_KEY = "_gpo_product_group";
    private final static String PARENT_PRODUCT_GROUP_KEY = "_gpo_parent_product_group";

    public OrderCreationRequestBody(List<ShoumanOrderContent> orderContents) {
        this.orderContents = orderContents;
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
        putNonNull(json, "country", "CHINA");
        putNonNull(json, "countryCode", "CN");
        // TODO: 2023/11/29 Change to real address
        putNonNull(json, "orderId", anyContent.getPlatformOrderId());
        JSONArray outboundInfos = new JSONArray();
        BigDecimal totalPrice = BigDecimal.ZERO;
        // Merge contents in case of necklaces with gems
        List<ShoumanOrderContent> reducedContents = mergeContentsForNecklaceWithGems(orderContents);
        for (ShoumanOrderContent content : reducedContents.isEmpty() ? orderContents : reducedContents) {
            JSONObject contentJson = new JSONObject();
            putNonNull(contentJson, "productName", content.getProductName());
            putNonNull(contentJson, "customerId", content.getPlatformOrderNumber());
            BigDecimal price = content.getPrice();
            putNonNull(contentJson, "price", price.toString());
            totalPrice = totalPrice.add(price);
            putNonNull(contentJson, "theImagePath", content.getImageUrl());
            putNonNull(contentJson, "comment", generateRemark(content.getRemark(), content.getCustomizationData(),
                    content.getRegexList(), content.getShopErpCode(), content.getPlatformOrderNumber()));
            putNonNull(contentJson, "sku", content.getSku());
            putNonNull(contentJson, "outboundNumder", content.getQuantity()); // Typo intended
            outboundInfos.add(contentJson);
        }
        putNonNull(json, "totalPrice", totalPrice.toString());
        putNonNull(json, "outboundInfos", outboundInfos);
        return json;
    }

    private String generateRemark(String baseRemark, String customizationData, List<ShoumanRegex> regexList,
                                  String shopErpCode, String platformOrderNumber) {
        StringBuilder sb = new StringBuilder();
        String[] baseRemarks = baseRemark.split(DEFAULT_SPLIT);
        for (String remark : baseRemarks) {
            sb.append(remark)
                    .append(LINE_BREAK);
        }

        for (ShoumanRegex regex : regexList) {
            String[] strings = customizationData.split(DEFAULT_SPLIT);
            int customCounter = 1;
            for (String s : strings) {
                if (s.matches(regex.getContentRecRegex())) {
                    String trimmed = s.trim();
                    String content = trimmed.split(regex.getContentExtRegex())[1];
                    if (regex.getIsSizeRegex().equalsIgnoreCase("1")) {
                        RingSize ringSize = RingSize.getBySize(Integer.parseInt(content));
                        if (ringSize != null) {
                            sb.append(ringSize.getText());
                        }
                    } else {
                        sb.append(regex.getPrefix())
                                .append(customCounter++)
                                .append(QUOTE)
                                .append(content)
                                .append(LINE_BREAK);
                    }
                }
            }
        }
        sb.append(SHOP_CODE)
                .append(shopErpCode)
                .append(LINE_BREAK);
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

    /**
     * In case of necklaces with gems, merging two contents into one :
     * First find two matching contents by comparing _gpo_product_group and _gpo_parent_product_group field values in
     * customization data
     * Then merge the necklace content into the gem's by simply replacing gem's custom data & base remark by the necklace's
     * So the resulting content will have the gem's SKU, and the necklace's custom data and base remark
     *
     * @param contents Original contents
     * @return Reduced contents
     */
    private List<ShoumanOrderContent> mergeContentsForNecklaceWithGems(List<ShoumanOrderContent> contents) {
        List<ShoumanOrderContent> reducedContents = new ArrayList<>();
        List<ShoumanOrderContent> necklaces = contents.stream().filter(ShoumanOrderContent::getIsNecklace).collect(Collectors.toList());
        List<ShoumanOrderContent> gems = contents.stream().filter(ShoumanOrderContent::getIsGem).collect(Collectors.toList());
        for (ShoumanOrderContent necklace : necklaces) {
            String necklacePgValue = extractProductGroupValue(necklace.getCustomizationData().split(DEFAULT_SPLIT), PRODUCT_GROUP_KEY);
            for (ShoumanOrderContent gem : gems) {
                String gemPgValue = extractProductGroupValue(gem.getCustomizationData().split(DEFAULT_SPLIT), PARENT_PRODUCT_GROUP_KEY);
                if (necklacePgValue != null && necklacePgValue.equalsIgnoreCase(gemPgValue)) {
                    // We need the remark from necklace for colour/material/month/font AND we need remark from
                    // gem for the mention of its presence
                    gem.setRemark(necklace.getRemark().concat(gem.getRemark()));
                    // We need the custom data from necklace to parse the name
                    gem.setCustomizationData(necklace.getCustomizationData());
                    reducedContents.add(gem);
                    break;
                }
            }
        }
        return reducedContents;
    }

    private String extractProductGroupValue(String[] strings, String key) {
        for (String string : strings) {
            if (string.startsWith(key)) {
                return string.split(QUOTE)[1];
            }
        }
        return null;
    }
}
