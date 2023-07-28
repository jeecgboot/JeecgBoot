package org.jeecg.modules.business.domain.api.hualei;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import static org.jeecg.modules.business.domain.api.ScanType.*;

@Slf4j
@Data
public class HLParcelTraceDetail {

    @JSONField(deserialize = false)
    private String parcelId;

    @JsonProperty("track_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
    private String scanTime;

    private String scanType;

    @JsonProperty("track_kind")
    private String trackKind;

    @JsonProperty("track_content")
    private String descriptionCn;

    private String descriptionEn;

    @JsonProperty("track_location")
    private String location;

    private static final String ARRIVAL_AT_RECEPTION_CN = "到达收货点";
    private static final String ARRIVAL_AT_RECEPTION = "Parcel has been handed to logistics company";
    private static final String DELIVERED = "Package delivered";
    private static final String ARRIVAL_LOCAL_DELIVERY = "Arrived at local delivery center";
    private static final String OUT_FOR_DELIVERY = "Out for delivery";
    private static final String LOCAL_DELIVERY_RECEPTION = "Received by local delivery company";
    private static final String DEPART_CUSTOMS = "Departed from customs";
    private static final String AWAITING_TRANSIT_TO_FINAL_DELIVERY_OFFICE = "Awaiting for transit to final delivery office";
    private static final String DEPARTED_FROM_TRANSIT_COUNTRY = "Departed from transit country/region";
    private static final String LEAVING_TRANSIT_COUNTRY = "Leaving transit country/region";
    private static final String IMPORT_CUSTOMS_CLEARANCE_COMPLETE = "Import customs clearance complete";
    private static final String IMPORT_CUSTOMS_CLEARANCE_STARTED = "Import customs clearance started";
    private static final String ARRIVAL_IN_TRANSIT_COUNTRY = "Arrived in transit country/region";
    private static final String HANDED_TO_LINEHAUL = "Handed over from linehaul office";
    private static final String ARRIVAL_LINEHAUL = "Arrived at linehaul office";
    private static final String DEPARTED_FROM_ORIGIN = "Departed from departure country/region";
    private static final String LEAVING_FROM_ORIGIN = "Leaving from departure country/region";
    private static final String EXPORT_CUSTOMS_CLEARANCE_COMPLETE = "Export customs clearance complete";
    private static final String EXPORT_CUSTOMS_CLEARANCE_START = "Export customs clearance started";
    private static final String TRANSPORT_HUB_ARRIVAL = "Arrived at departure transport hub";
    private static final String SORTING_CENTER_DEPARTURE = "[Fenggang Town] Departed from sorting center";
    private static final String SORTING_CENTER_PROCESSING = "[Fenggang Town] Processing at sorting center";
    private static final String ORDER_RECEIVED = "货物电子信息已经收到";
    private static final String ORDER_RECEIVED_EN = "Order received by logistics company";


    public HLParcelTraceDetail() {
    }

    public HLParcelTraceDetail(String parcelId, String scanTime, String scanType, String trackKind,
                               String descriptionCn, String descriptionEn, String location) {
        this.parcelId = parcelId;
        this.scanTime = scanTime;
        this.scanType = scanType;
        this.trackKind = trackKind;
        this.descriptionCn = descriptionCn;
        this.descriptionEn = descriptionEn;
        this.location = location;
    }

    /**
     * Set parcel ID, and write the scan type into the trace according to description
     * @param parcelId Parcel ID to which the trace belongs to
     */
    public void parcelTraceProcess(String parcelId) {
        setParcelId(parcelId);
        switch (descriptionCn) {
            case DELIVERED:
                setScanType(END_DELIVERED.getDesc());
                break;
            case ARRIVAL_LOCAL_DELIVERY:
                setScanType(END_ARRIVED.getDesc());
                break;
            case OUT_FOR_DELIVERY:
                setScanType(END_DELIVERY.getDesc());
                break;
            case LOCAL_DELIVERY_RECEPTION:
                setScanType(END_RECEIVED.getDesc());
                break;
            case DEPART_CUSTOMS:
            case IMPORT_CUSTOMS_CLEARANCE_COMPLETE:
                setScanType(CUSTOMS_CLEARANCE_COMPLETED.getDesc());
                break;
            case AWAITING_TRANSIT_TO_FINAL_DELIVERY_OFFICE:
                setScanType(END_SEND.getDesc());
                break;
            case DEPARTED_FROM_TRANSIT_COUNTRY:
            case LEAVING_TRANSIT_COUNTRY:
            case ARRIVAL_IN_TRANSIT_COUNTRY:
                setScanType(FLIGHT_ARRIVED.getDesc());
                break;
            case IMPORT_CUSTOMS_CLEARANCE_STARTED:
                setScanType(CUSTOMS_CLEARANCE.getDesc());
                break;
            case HANDED_TO_LINEHAUL:
                setScanType(FLIGHT_DEPARTURE.getDesc());
                break;
            case ARRIVAL_LINEHAUL:
            case DEPARTED_FROM_ORIGIN:
            case LEAVING_FROM_ORIGIN:
                setScanType(FLIGHT_PREPARING.getDesc());
                break;
            case EXPORT_CUSTOMS_CLEARANCE_COMPLETE:
                setScanType(EXPORT_CUSTOMS_DECLARATION_COMPLETED.getDesc());
                break;
            case EXPORT_CUSTOMS_CLEARANCE_START:
                setScanType(EXPORT_CUSTOMS_DECLARATION.getDesc());
                break;
            case TRANSPORT_HUB_ARRIVAL:
            case SORTING_CENTER_DEPARTURE:
                setScanType(FACILITY_DEPARTURE.getDesc());
                break;
            case SORTING_CENTER_PROCESSING:
                setScanType(FACILITY_ARRIVED.getDesc());
                break;
            case ORDER_RECEIVED:
                setDescriptionEn(ORDER_RECEIVED_EN);
                setScanType(ORDER_PLACED.getDesc());
                break;
            case ARRIVAL_AT_RECEPTION_CN:
                setDescriptionEn(ARRIVAL_AT_RECEPTION);
                setScanType(BAGGING.getDesc());
                break;
        }
    }
}
