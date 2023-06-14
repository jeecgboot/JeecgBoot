package org.jeecg.modules.business.domain.api.cmk;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.ScanType;

import java.util.Objects;

import static org.jeecg.modules.business.domain.api.ScanType.*;

@Slf4j
@Data
public class CMKParcelTrace {

    @JSONField(deserialize = false)
    private String parcelId;

    @JsonProperty("desc")
    private String description;

    private String descriptionEn;

    private String time;
    /**
     * 位置
     */
    @JsonProperty("localtion")
    private String location;

    private String scanType;

    public static final String VENTE_EN_LIGNE_TO_IGNORE = "009910,VENTE ON LINE";
    public static final String VENTE_EN_LIGNE_CN_TO_IGNORE = "该包裹在法邮创建，等待送往法邮";
    public static final String CUSTOM_CLEARED_CN_TO_IGNORE = "您的包裹已清关";

    public static final String CMK = "Shipment information sent to CMK";
    public static final String SHIPMENT_INFO_RECEIVED = "Shipment information received";
    public static final String ARRIVE_FACILITY = "Arrived at Facility";
    public static final String PROCESSED_FACILITY = "Processed at Facility";
    public static final String DEPART_FACILITY = "Departed from Facility";
    public static final String PACKED = "已打包";
    public static final String PACKED_EN = "Bagging";
    public static final String ARRIVE_AIRPORT_ABROAD = "Arrive at international airport to abroad";
    public static final String DEPART_AIRPORT_ABROAD = "Departed from AIRPORT of Origin";
    public static final String ARRIVE_AIRPORT_DESTINATION = "Arrived at AIRPORT of Destination";
    public static final String CUSTOM_CLEARED = "Custom clearance completed";
    public static final String CUSTOM_CLEARED_CN = "清关完成";
    public static final String END_ARRIVED = "Delivery to local courier";
    public static final String END_RECEIVED = "法邮已收取快件， 派送中";
    public static final String END_RECEIVED_EN = "Shipment given to local courier";
    public static final String ARRIVED_AT_DELIVERY = "包裹已到达分拣中心";
    public static final String ARRIVED_AT_DELIVERY_EN = "Shipment arrived at distribution center";
    public static final String DELIVERY_SCHEDULED = "包裹将在收件人选择的日期预约递送";
    public static final String DELIVERY_SCHEDULED_2 = "包裹将在收件人选择的日期通过预约递送";
    public static final String DELIVERY_SCHEDULED_EN = "Package will be delivered on date chosen by consignee";
    public static final String DELIVERY_DELAYED = "包裹今天无法送达.将尽快安排再次配送";
    public static final String DELIVERY_DELAYED_EN = "Package can not be delivered today, another attempt will be arranged as soon as possible";
    public static final String DELIVERY_DELAYED_2 = "由于收件人地址的问题,包裹今天无法送达.将尽快安排再次配送";
    public static final String DELIVERY_DELAYED_2_EN = "Due to a problem with the address, package can not be delivered today, another attempt will be arranged as soon as possible";
    public static final String DELIVERY_DELAYED_3 = "由于无法控制的特殊情况,包裹今天无法送达.将尽快安排再次配送";
    public static final String DELIVERY_DELAYED_3_EN = "Due to a circumstances beyond our control, package can not be delivered today, another attempt will be arranged as soon as possible";
    public static final String CONSIGNEE_ABSENT = "由于收件人不在,您的包裹无法送达.它将在下一个工作日再次交付";
    public static final String CONSIGNEE_ABSENT_EN = "Consignee absent, package can not be delivered today, another attempt will be made the next working day";
    public static final String DELIVERY_NEXT_WORKING_DAY = "包裹将在下一个工作日送达。 如果您不在,请在www.colissimo.fr/monchoix告诉我们。";
    public static final String DELIVERY_NEXT_WORKING_DAY_EN = "Package will be delivered the next working day. If absent, please inform us at www.colissimo.fr/monchoix";
    public static final String DELIVERY_STATION = "您的包裹位于支持您地址的送货站点。我们准备交付。";
    public static final String DELIVERY_STATION_EN = "Package has arrived at delivery site.";
    public static final String DELIVERED = "包裹已送达";
    public static final String DELIVERED_EN = "Delivered";
    public static final String DELIVERED_TO_CONCIERGE = "包裹已由楼层管理员签收";
    public static final String DELIVERED_TO_CONCIERGE_EN = "Delivered to concierge";
    public static final String INSTRUCTIONS_RECEIVED = "包裹根据收件人的要求重新送至其指定的地点";
    public static final String INSTRUCTIONS_RECEIVED_EN = "Package is being redirected to instructed address";
    public static final String TO_BE_RETRIEVED = "包裹已投放至所选择的取货点, 收件人需10日携带有效证件前往领取";
    public static final String TO_BE_RETRIEVED_EN = "Package has been left at relay point chosen by consignee and can be retrieved in the next 10 working days";
    public static final String END_ABNORMAL_ADDRESS = "由于地址问题，您的包裹无法交付给您。它将退还给发件人。";
    public static final String END_ABNORMAL_ADDRESS_EN = "Due to a problem with the address, the package can not be delivered and will be returned to sender";
    public static final String END_ABNORMAL_ADDRESS_INCOMPLETE = "收件人地址信息不完整,无法送达.请联系客服部门补充地址信息";
    public static final String END_ABNORMAL_ADDRESS_INCOMPLETE_EN = "Package can not be delivered due to incomplete consignee address, please contact customer service";
    public static final String END_ABNORMAL_PICKING = "包裹分拣错误,现已重新派送";
    public static final String END_ABNORMAL_PICKING_EN = "Picking error, package will be re-delivered";
    public static final String END_RETURN_TO_SENDER = "包裹退回,送还至发件人";
    public static final String END_RETURN_TO_SENDER_EN = "Return to sender";
    public static final String END_RETURN = "包裹退回";
    public static final String END_RETURN_EN = "Returned";


    public CMKParcelTrace() {
    }

    public void parcelTraceProcess(String parcelId) {
        setParcelId(parcelId);
        switch (description) {
            case CMK:
                setDescriptionEn(SHIPMENT_INFO_RECEIVED);
                setScanType(ORDER_PLACED.getDesc());
                break;
            case PACKED:
                setDescriptionEn(PACKED_EN);
                setScanType(BAGGING.getDesc());
                break;
            case ARRIVE_FACILITY:
                setScanType(FACILITY_ARRIVED.getDesc());
                break;
            case PROCESSED_FACILITY:
                setScanType(FACILITY_OUTBOUND.getDesc());
                break;
            case DEPART_FACILITY:
                setScanType(FACILITY_DEPARTURE.getDesc());
                break;
            case ARRIVE_AIRPORT_ABROAD:
                setScanType(ARRIVED_PORT.getDesc());
                break;
            case DEPART_AIRPORT_ABROAD:
                setScanType(FLIGHT_DEPARTURE.getDesc());
                break;
            case ARRIVE_AIRPORT_DESTINATION:
                setScanType(FLIGHT_ARRIVED.getDesc());
                break;
            case CUSTOM_CLEARED:
                setScanType(CUSTOMS_CLEARANCE_COMPLETED.getDesc());
                break;
            case CUSTOM_CLEARED_CN:
                setDescriptionEn(CUSTOM_CLEARED);
                setScanType(CUSTOMS_CLEARANCE_COMPLETED.getDesc());
                break;
            case END_ARRIVED:
                setScanType(ScanType.END_ARRIVED.getDesc());
                break;
            case END_RECEIVED:
                setDescriptionEn(END_RECEIVED_EN);
                setScanType(ScanType.END_RECEIVED.getDesc());
                break;
            case ARRIVED_AT_DELIVERY:
                setDescriptionEn(ARRIVED_AT_DELIVERY_EN);
                setScanType(END_DELIVERY.getDesc());
                break;
            case DELIVERY_STATION:
                setDescriptionEn(DELIVERY_STATION_EN);
                setScanType(END_DELIVERY.getDesc());
                break;
            case INSTRUCTIONS_RECEIVED:
                setDescriptionEn(INSTRUCTIONS_RECEIVED_EN);
                setScanType(END_DELIVERY.getDesc());
                break;
            case DELIVERY_SCHEDULED:
            case DELIVERY_SCHEDULED_2:
                setDescriptionEn(DELIVERY_SCHEDULED_EN);
                setScanType(END_DELIVERY.getDesc());
                break;
            case DELIVERY_NEXT_WORKING_DAY:
                setDescriptionEn(DELIVERY_NEXT_WORKING_DAY_EN);
                setScanType(END_DELIVERY.getDesc());
                break;
            case DELIVERY_DELAYED:
                setDescriptionEn(DELIVERY_DELAYED_EN);
                setScanType(END_ABNORMAL.getDesc());
                break;
            case DELIVERY_DELAYED_2:
                setDescriptionEn(DELIVERY_DELAYED_2_EN);
                setScanType(END_ABNORMAL.getDesc());
                break;
            case DELIVERY_DELAYED_3:
                setDescriptionEn(DELIVERY_DELAYED_3_EN);
                setScanType(END_ABNORMAL.getDesc());
                break;
            case CONSIGNEE_ABSENT:
                setDescriptionEn(CONSIGNEE_ABSENT_EN);
                setScanType(END_ABNORMAL.getDesc());
                break;
            case DELIVERED:
                setDescriptionEn(DELIVERED_EN);
                setScanType(END_DELIVERED.getDesc());
                break;
            case DELIVERED_TO_CONCIERGE:
                setDescriptionEn(DELIVERED_TO_CONCIERGE_EN);
                setScanType(END_DELIVERED.getDesc());
                break;
            case TO_BE_RETRIEVED:
                setDescriptionEn(TO_BE_RETRIEVED_EN);
                setScanType(END_DELIVERED.getDesc());
                break;
            case END_ABNORMAL_ADDRESS:
                setDescriptionEn(END_ABNORMAL_ADDRESS_EN);
                setScanType(END_ABNORMAL.getDesc());
                break;
            case END_ABNORMAL_PICKING:
                setDescriptionEn(END_ABNORMAL_PICKING_EN);
                setScanType(END_ABNORMAL.getDesc());
                break;
            case END_ABNORMAL_ADDRESS_INCOMPLETE:
                setDescriptionEn(END_ABNORMAL_ADDRESS_INCOMPLETE_EN);
                setScanType(END_ABNORMAL.getDesc());
                break;
            case END_RETURN:
                setDescriptionEn(END_RETURN_EN);
                setScanType(RETURN.getDesc());
                break;
            case END_RETURN_TO_SENDER:
                setDescriptionEn(END_RETURN_TO_SENDER_EN);
                setScanType(RETURN.getDesc());
                break;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CMKParcelTrace that = (CMKParcelTrace) o;
        return Objects.equals(description, that.description) && Objects.equals(time, that.time) && Objects.equals(location, that.location);
    }

    public boolean isUseful() {
        return !getDescription().equalsIgnoreCase(VENTE_EN_LIGNE_CN_TO_IGNORE)
                && !getLocation().equalsIgnoreCase(VENTE_EN_LIGNE_TO_IGNORE)
                && !getDescription().equalsIgnoreCase(CUSTOM_CLEARED_CN_TO_IGNORE);
    }
}
