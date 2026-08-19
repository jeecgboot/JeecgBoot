package org.jeecg.modules.business.domain.api.shouman;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.jeecg.modules.business.entity.Shouman.ShoumanOrderBase;
import org.jeecg.modules.business.entity.Shouman.ShoumanOrderContent;
import org.jeecg.modules.business.entity.ShoumanRegex;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
public class OrderCreationRequestBody implements RequestBody {

    private ShoumanOrderBase shoumanOrderBase;

    private final static String DEFAULT_SPLIT = ";";
    private final static String LINE_BREAK = "\n";
    private final static String QUOTE = ":";
    private final static String WIA = "维亚智通";
    private final static String DROP_SHIPPING = "海外代发";
    private final static String TRANSACTION_NUMBER = "交易号";
    private final static String SHOP_CODE = "店铺名称";
    private final static String CUSTOM_PHOTO_URL = "定制照片链接";
    private final static Pattern DESIGN_ID_PATTERN = Pattern.compile("[?&]d=([^&\\s]+)");
    private final static Pattern PRINT_4K_PATTERN = Pattern.compile("^\\s*Print 4K:\\s*(\\S+)\\s*$", Pattern.MULTILINE);
    private final static Pattern DESIGN_ID_IN_CUSTOMIZATION_PATTERN = Pattern.compile("(?:^|;)\\s*_designId:([^;]+)");
    private final static Pattern URL_PATTERN = Pattern.compile("https?://\\S+");
    private final static String PRODUCT_GROUP_KEY = "_gpo_product_group";
    private final static String PARENT_PRODUCT_GROUP_KEY = "_gpo_parent_product_group";
    private final static Pattern INCH_RANGE_PATTERN = Pattern.compile(
            "(\\d+(?:\\.\\d+)?)\\s*-\\s*(\\d+(?:\\.\\d+)?)\\s*\"");

    public OrderCreationRequestBody(ShoumanOrderBase shoumanOrderBase) {
        this.shoumanOrderBase = shoumanOrderBase;
    }

    @Override
    public String path() {
        return "/order/openapi/orders/add";
    }

    @Override
    public JSONObject parameters() {
        JSONObject json = new JSONObject();
        // TODO: 2023/11/29 Change to real address
        putNonNull(json, "address", "收货人：王生 收货地址： 广东省东镇芦莞市寮步溪二路40号汇元佳科技园二栋10楼公司前台 手机：17633551138");
        putNonNull(json, "addressee", "王生");
        putNonNull(json, "city", "东莞市");
        putNonNull(json, "country", "CHINA");
        putNonNull(json, "countryCode", "CN");
        // TODO: 2023/11/29 Change to real address
        putNonNull(json, "orderId", shoumanOrderBase.getPlatformOrderId());
        JSONArray outboundInfos = new JSONArray();
        BigDecimal totalPrice = BigDecimal.ZERO;
        StringBuilder memo = new StringBuilder();
        // Merge contents in case of necklaces with gems
        List<ShoumanOrderContent> allContents = shoumanOrderBase.getContentList();
        List<ShoumanOrderContent> reducedContents = mergeContentsForNecklaceWithGems(allContents);
        for (ShoumanOrderContent content : reducedContents.isEmpty() ? allContents : reducedContents) {
            if (content.getIsMemo()) {
                if (!memo.isEmpty()) memo.append(LINE_BREAK);
                memo.append(content.getRemark());
                continue;
            }
            JSONObject contentJson = new JSONObject();
            putNonNull(contentJson, "productName", content.getProductName());
            putNonNull(contentJson, "customerId", shoumanOrderBase.getPlatformOrderNumber().replace("MS", ""));
            BigDecimal price = content.getPrice();
            putNonNull(contentJson, "price", price.toString());
            totalPrice = totalPrice.add(price);
            putNonNull(contentJson, "theImagePath", content.getImageUrl());
            putNonNull(contentJson, "comment", generateRemark(content.getRemark(), content.getCustomizationData(),
                    content.getRegexList(), shoumanOrderBase.getShopErpCode(), shoumanOrderBase.getPlatformOrderNumber(),
                    content.getCustomizationUrl()));
            if (shoumanOrderBase.getBuyerMessage() != null) {
                String manuscriptCustomizationData = resolveManuscriptCustomizationData(content, allContents);
                addManuscripts(contentJson, manuscriptCustomizationData, content.getRegexList(),
                    shoumanOrderBase.getBuyerMessage());
            }
            putNonNull(contentJson, "sku", content.getSku());
            putNonNull(contentJson, "outboundNumder", content.getQuantity()); // Typo intended
            outboundInfos.add(contentJson);
        }
        putNonNull(json, "totalPrice", totalPrice.toString());
        putNonNull(json, "outboundInfos", outboundInfos);
        if (shoumanOrderBase.getShippingLabelUrl() != null) {
            JSONObject logisticsInfo = new JSONObject();
            putNonNull(logisticsInfo, "carrierName", shoumanOrderBase.getLogisticChannelName());
            putNonNull(logisticsInfo, "carrierCode", shoumanOrderBase.getLogisticChannelCode());
            putNonNull(logisticsInfo, "trackingNumber", shoumanOrderBase.getTrackingNumber());
            putNonNull(logisticsInfo, "labelUrl", shoumanOrderBase.getShippingLabelUrl());
            putNonNull(json, "logisticsInfo", logisticsInfo);
        }
        if (!memo.isEmpty()) {
            putNonNull(json, "orderMemo", memo.toString());
        }
        return json;
    }

    private String resolveManuscriptCustomizationData(ShoumanOrderContent content,
                                                       List<ShoumanOrderContent> allContents) {
        if (content.getCustomizationData() != null && !content.getCustomizationData().trim().isEmpty()) {
            return content.getCustomizationData();
        }
        if (content.getSkuId() == null || allContents == null) {
            return null;
        }
        return allContents.stream()
                .filter(candidate -> content.getSkuId().equals(candidate.getSkuId()))
                .map(ShoumanOrderContent::getCustomizationData)
                .filter(customizationData -> customizationData != null && !customizationData.trim().isEmpty())
                .findFirst()
                .orElse(null);
    }
    private void addManuscripts(JSONObject contentJson, String customizationData, List<ShoumanRegex> regexList,
                                String buyerMessage) {
        if (buyerMessage == null || buyerMessage.trim().isEmpty()
                || customizationData == null || customizationData.trim().isEmpty()) {
            return;
        }

        JSONArray manuscripts = new JSONArray();
        Set<String> manuscriptUrls = new LinkedHashSet<>();
        addBuyerMessageManuscripts(manuscriptUrls, customizationData, regexList, buyerMessage);
        for (String manuscriptUrl : manuscriptUrls) {
            JSONObject manuscript = new JSONObject();
            manuscript.put("manuscriptUrl", manuscriptUrl);
            manuscript.put("thumbnailUrl", manuscriptUrl);
            manuscripts.add(manuscript);
        }

        // Keep supporting the original design-id based Print 4K format.
        if (manuscripts.isEmpty()) {
            addPrint4kManuscript(manuscripts, customizationData, buyerMessage);
        }
        if (!manuscripts.isEmpty()) {
            contentJson.put("manuscripts", manuscripts);
        }
    }

    private void addBuyerMessageManuscripts(Set<String> manuscriptUrls, String customizationData,
                                             List<ShoumanRegex> regexList, String buyerMessage) {
        if (regexList == null) {
            return;
        }
        for (ShoumanRegex regex : regexList) {
            if (!isEnabled(regex.getUseOnBuyerMessage())) {
                continue;
            }
            for (String customizationPart : customizationData.split(DEFAULT_SPLIT)) {
                if (!customizationPart.matches(regex.getContentRecRegex())) {
                    continue;
                }
                String extractedContent = extractCustomizationContent(customizationPart, regex.getContentExtRegex());
                if (extractedContent == null || extractedContent.trim().isEmpty()) {
                    continue;
                }
                String normalizedContent = extractedContent.trim().toLowerCase();
                Matcher urlMatcher = URL_PATTERN.matcher(buyerMessage);
                while (urlMatcher.find()) {
                    String url = urlMatcher.group();
                    if (url.toLowerCase().contains(normalizedContent)) {
                        manuscriptUrls.add(url);
                    }
                }
            }
        }
    }

    private String extractCustomizationContent(String customizationPart, String contentExtRegex) {
        String[] extractedParts = customizationPart.trim().split(contentExtRegex);
        return extractedParts.length == 0 ? null : extractedParts[extractedParts.length - 1].trim();
    }

    private void addPrint4kManuscript(JSONArray manuscripts, String customizationData, String buyerMessage) {
        Matcher designIdMatcher = DESIGN_ID_IN_CUSTOMIZATION_PATTERN.matcher(customizationData);
        if (!designIdMatcher.find()) {
            return;
        }
        String designId = designIdMatcher.group(1).trim();

        Matcher printMatcher = PRINT_4K_PATTERN.matcher(buyerMessage);
        while (printMatcher.find()) {
            String printUrl = printMatcher.group(1).trim();
            Matcher urlDesignIdMatcher = DESIGN_ID_PATTERN.matcher(printUrl);
            if (!urlDesignIdMatcher.find() || !designId.equals(urlDesignIdMatcher.group(1))) {
                continue;
            }
            JSONObject manuscript = new JSONObject();
            manuscript.put("manuscriptUrl", printUrl);
            manuscript.put("thumbnailUrl", printUrl);
            manuscripts.add(manuscript);
            break;
        }
    }
    private String generateRemark(String baseRemark, String customizationData, List<ShoumanRegex> regexList,
                                  String shopErpCode, String platformOrderNumber, String customizationURL) {
        StringBuilder sb = new StringBuilder();
        if (customizationData == null) {
            customizationData = "";
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
        // VA 所有店铺订单都添加 海外代发
        if (shopErpCode.contains("VA")) {
            sb.append(DROP_SHIPPING);
        }
        sb.append(LINE_BREAK);

        String[] baseRemarks = baseRemark.split(DEFAULT_SPLIT);
        for (String remark : baseRemarks) {
            sb.append(remark)
                    .append(LINE_BREAK);
        }

        if (shoumanOrderBase.getBuyerMessage() != null && !shoumanOrderBase.getBuyerMessage().trim().isEmpty()) {
            return sb.toString();
        }
        for (ShoumanRegex regex : regexList) {
            String[] strings = customizationData.split(DEFAULT_SPLIT);
            int customCounter = 1;
            List<String> commaSeparatedContents = new ArrayList<>();
            for (String s : strings) {
                if (s.matches(regex.getContentRecRegex())) {
                    String trimmed = s.trim();
                    String[] split = trimmed.split(regex.getContentExtRegex());
                    String content = split[split.length - 1];
                    if (isEnabled(regex.getIsSizeRegex())) {
                        RingSize ringSize = RingSize.getBySize(content);
                        if (ringSize != null) {
                            sb.append(ringSize.getText())
                                    .append(LINE_BREAK);
                        }
                    } else {
                        if (content.isEmpty()) continue;
                        String remarkContent = convertRemarkContent(regex, content);
                        if (isEnabled(regex.getIsCommaSeparated())) {
                            commaSeparatedContents.add(remarkContent);
                        } else {
                            sb.append(regex.getPrefix())
                                    .append(customCounter++)
                                    .append(QUOTE)
                                    .append(remarkContent)
                                    .append(LINE_BREAK);
                        }
                    }
                }
            }
            if (!commaSeparatedContents.isEmpty()) {
                sb.append(regex.getPrefix())
                        .append(QUOTE)
                        .append(String.join(",", commaSeparatedContents))
                        .append(LINE_BREAK);
            }
        }
        if (customizationURL != null && !customizationURL.isEmpty()) {
            sb.append(CUSTOM_PHOTO_URL)
                    .append(QUOTE)
                    .append(customizationURL);
        }
        return sb.toString();
    }

    private String convertRemarkContent(ShoumanRegex regex, String content) {
        if (isEnabled(regex.getIsMonthRegex())) {
            BirthStone birthStone = BirthStone.getByName(content);
            if (birthStone != null) {
                return birthStone.getChinese();
            }
            return "";
        }
        if (isEnabled(regex.getIsInInches())) {
            return convertInchRangeToCentimeters(content);
        }
        return content;
    }

    private String convertInchRangeToCentimeters(String content) {
        Matcher matcher = INCH_RANGE_PATTERN.matcher(content);
        if (!matcher.find()) {
            return content;
        }

        String lowerBound = inchesToCentimeters(matcher.group(1));
        String upperBound = inchesToCentimeters(matcher.group(2));
        return matcher.replaceFirst(Matcher.quoteReplacement(lowerBound + "-" + upperBound + "cm"));
    }

    private String inchesToCentimeters(String inches) {
        return new BigDecimal(inches)
                .multiply(new BigDecimal("2.54"))
                .setScale(0, RoundingMode.DOWN)
                .toPlainString();
    }

    private boolean isEnabled(String value) {
        return "1".equals(value);
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
