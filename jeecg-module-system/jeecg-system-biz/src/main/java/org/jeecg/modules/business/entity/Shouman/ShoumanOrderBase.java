package org.jeecg.modules.business.entity.Shouman;

import lombok.Data;
import org.jeecg.modules.business.entity.ShoumanRegex;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ShoumanOrderBase {

    private String shopErpCode;
    private String platformOrderId;
    private String platformOrderNumber;
    private String postcode;
    private String recipient;
    private String city;
    private String country;
    private String trackingNumber;
    private String logisticChannelName;
    private String logisticChannelCode;
    private String shippingLabelUrl;
    private String buyerMessage;
    private List<ShoumanOrderContent> contentList;

    public ShoumanOrderBase() {
    }

    @Override
    public String toString() {
        return "ShoumanOrderContent{" +
                "shopErpCode='" + shopErpCode + '\'' +
                "platformOrderId='" + platformOrderId + '\'' +
                "platformOrderNumber='" + platformOrderNumber + '\'' +
                ", postcode='" + postcode + '\'' +
                ", recipient='" + recipient + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", trackingNumber='" + trackingNumber + '\'' +
                ", logisticChannelName='" + logisticChannelName + '\'' +
                ", logisticChannelCode='" + logisticChannelCode + '\'' +
                ", shippingLabelUrl='" + shippingLabelUrl + '\'' +
                ", buyerMessage='" + buyerMessage + '\'' +
                ", contentList='" + contentList + '\'' +
                '}';
    }
}
