package org.jeecg.modules.business.domain.api.mabang.getorderlist;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;
import java.util.Objects;

/**
 * Data container of platform Order, mabang side,
 * annotation is JSON key of mabang.
 * <p>
 * This is a domain object.
 */
@Data
@TableName("platform_order")
public class Order {
    /**
     * Primary key
     */
    @JSONField(deserialize = false)
    @TableId(type = IdType.ASSIGN_ID)
    private String id = IdWorker.getIdStr();

    /**
     * Shop name is correspondent the shop erp code in our database
     */
    @JSONField(name = "shopName")
    private String shopErpCode;

    /**
     * 物流渠道
     */
    @JSONField(name = "myLogisticsChannelName")
    private String logisticChannelName;
    /**
     * 平台订单号码
     */
    @JSONField(name = "platformOrderId")
    private String platformOrderId;

    @JSONField(name = "salesRecordNumber")
    private String platformOrderNumber;

    @JSONField(name = "erpOrderId")
    private String erpOrderId;

    /**
     * If tracking is empty, set it null
     */
    @JSONField(name = "trackNumber")
    private String trackingNumber;

    /**
     * If tracking is empty, set it null
     */
    @JSONField(name = "trackNumber1")
    private String trackingNumber1;

    @JSONField(name = "paidTime", format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "fr", timezone = "GMT+1")
    private String orderTime;

    @JSONField(name = "expressTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "fr", timezone = "GMT+1")
    private String shippingTime;

    /**
     * 订单收件人
     */
    @JSONField(name = "buyerName")
    private String recipient;
    /**
     * 订单收件人国家
     */
    @JSONField(name = "countryNameEN")
    private String country;
    /**
     * 订单收件人邮编
     */
    @JSONField(name = "postCode")
    private String postcode;

    /**
     * 状态
     */
    @JSONField(name = "orderStatus")
    private String status;

    @JSONField(name = "canSend")
    private String canSend;

    @JSONField(name = "isUnion")
    private String isUnion;

    @JSONField(name = "isNewOrder")
    private String isNewOrder;

    /**
     * 1 = 有货
     * 2 = 缺货
     * 3 = 已补货
     */
    @JSONField(name = "hasGoods")
    private String hasGoods;

    /**
     * Product available = 1, unavailable = 0
     */
    private String productAvailable;

    @JSONField(name = "orderItem")
    @TableField(exist = false)
    private List<OrderItem> orderItems;

    @JSONField(name = "phone1")
    private String phone1;

    public void setTrackingNumber(String trackingNumber) {
        if (trackingNumber != null && trackingNumber.length() == 0) {
            this.trackingNumber = null;
        } else
            this.trackingNumber = trackingNumber;
    }

    public void setTrackingNumber1(String trackingNumber) {
        if (trackingNumber != null && trackingNumber.length() == 0) {
            this.trackingNumber1 = null;
        } else
            this.trackingNumber1 = trackingNumber;
    }

    public boolean isUnion() {
        return isUnion.equals("1");
    }

    public boolean isPending() {
        return isNewOrder.equals("1") && status.equals("2");
    }

    public boolean isPreparing() {
        return isNewOrder.equals("2") && status.equals("2");
    }

    /**
     * Another order is source only if they have the same recipient, postcode, country.
     *
     * @param candidate candidate
     * @return true if candidate is a merge source, false otherwise
     */
    public boolean isSource(Order candidate) {
        return Objects.equals(recipient, candidate.recipient)
                && Objects.equals(postcode, candidate.postcode)
                && Objects.equals(country, candidate.country);
    }

    public void setShippingTime(String shippingTime) {
        if (shippingTime != null && shippingTime.length() == 0) {
            this.shippingTime = null;
        } else
            this.shippingTime = shippingTime;
    }

    /**
     * As of 2021-06-18, according to Mabang's API documentation, the "status" in request parameters is NOT the
     * equivalent of "orderStatus" in the API response.
     * "orderStatus" : 1.风控中 2.配货中 3.已发货 4.已完成 5.已作废
     * "status" : 1.待处理 2.配货中 3.已发货 4.已完成 5.已作废 6.所有未发货 7.所有非未发货 默认配货中订单
     * Hence even if we explicitly request orders in the status of 待处理 (=1), the orderStatus in return will be 配货中(=2)
     * Therefore, in order to distinguish orders of status 待处理 and 配货中, we must rely on the following attributes :
     * "isNewOrder" : 1.待处理订单 2.配货中订单 and "orderStatus" mentioned before.
     */
    public void resolveStatus() {
        if (isPending()) {
            this.status = OrderStatus.Pending.getCode();
        } else if (isPreparing()) {
            this.status = OrderStatus.Preparing.getCode();
        }
    }

    public void resolveProductAvailability() {
        if (hasGoods.equalsIgnoreCase("2")) {
            productAvailable = "0";
        } else {
            productAvailable = "1";
        }
    }
}
