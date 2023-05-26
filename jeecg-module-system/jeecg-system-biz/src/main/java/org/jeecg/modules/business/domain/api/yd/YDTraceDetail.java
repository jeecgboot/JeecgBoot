package org.jeecg.modules.business.domain.api.yd;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import static org.jeecg.modules.business.domain.api.ScanType.*;

@Slf4j
@Data
public class YDTraceDetail {

    @JSONField(deserialize = false)
    private String parcelId;

    @JsonProperty("track_occur_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
    private String scanTime;

    @JsonProperty("track_description")
    private String descriptionCn;

    @JsonProperty("track_description_en")
    private String descriptionEn;

    @JsonProperty("track_location")
    private String traceLocation;

    @JsonProperty("track_status")
    private String traceStatus;

    @JsonProperty("track_code")
    private String traceCode;

    @JsonProperty("tbs_id")
    private String orderNo;

    @JsonProperty("track_server_code")
    private String productCode;

    private String scanType;

    public static final String CUSTOMS_INSPECTION = "AC";
    public static final String DELAY = "AD";
    public static final String ARRIVE_FINAL_DESTINATION = "AF";
    public static final String TRANSPORT_DELAY = "AT";
    public static final String OUT_FOR_DELIVERY = "ND";
    public static final String IN_TRANSIT = "NT";
    public static final String CUSTOMS_CLEARED = "NC";
    public static final String ARRIVE_NEARBY_HUB = "NN";
    public static final String DELIVERED = "CC";
    public static final String REFUSED = "RR";
    public static final String RETURN_TO_SHIPPER = "RT";
    private final String ARRIVED_AIRPORT = "AA";
    private final String DEPART_AIRPORT = "DA";
    private final String ARRIVED_FACILITY = "AF";
    private final String DEPART_FACILITY = "DF";
    private final String NEW_ORDER = "NO";

    public YDTraceDetail() {
    }

    public void parcelTraceProcess(String parcelId) {
        setParcelId(parcelId);
        switch (traceStatus) {
            case CUSTOMS_INSPECTION:
                setScanType(CN_CUSTOMS_INSPECTION.getDesc());
                break;
            case DELAY:
                if (REFUSED.equalsIgnoreCase(traceCode) || RETURN_TO_SHIPPER.equalsIgnoreCase(traceCode)) {
                    setScanType(RETURN.getDesc());
                } else {
                    setScanType(END_ABNORMAL.getDesc());
                }
                break;
            case TRANSPORT_DELAY:
                setScanType(FLIGHT_DELAY.getDesc());
                break;
            case DELIVERED:
                setScanType(END_DELIVERED.getDesc());
                break;
            case OUT_FOR_DELIVERY:
                setScanType(END_DELIVERY.getDesc());
                break;
            case ARRIVE_FINAL_DESTINATION:
                setScanType(END_RECEIVED.getDesc());
                break;
            case IN_TRANSIT:
                if (ARRIVED_AIRPORT.equalsIgnoreCase(traceCode)) {
                    setScanType(FLIGHT_ARRIVED.getDesc());
                    break;
                } else if (DEPART_AIRPORT.equalsIgnoreCase(traceCode)) {
                    setScanType(FLIGHT_DEPARTURE.getDesc());
                    break;
                } else if (ARRIVE_NEARBY_HUB.equalsIgnoreCase(traceCode)){
                    setScanType(END_ARRIVED.getDesc());
                    break;
                } else if (ARRIVED_FACILITY.equalsIgnoreCase(traceCode)){
                    setScanType(FACILITY_ARRIVED.getDesc());
                    break;
                } else if (DEPART_FACILITY.equalsIgnoreCase(traceCode)) {
                    setScanType(FACILITY_DEPARTURE.getDesc());
                    break;
                }
                break;
            case CUSTOMS_CLEARED:
                setScanType(CUSTOMS_CLEARANCE_COMPLETED.getDesc());
                break;
            case NEW_ORDER:
                setScanType(ORDER_PLACED.getDesc());
                break;
        }
    }

    public boolean equals(Object anotherDetail) {
        if (anotherDetail instanceof YDTraceDetail) {
            YDTraceDetail another = (YDTraceDetail) anotherDetail;
            return another.getParcelId().equalsIgnoreCase(this.parcelId)
                    && another.getTraceCode().equalsIgnoreCase(this.traceCode)
                    && another.getScanTime().equalsIgnoreCase(this.scanTime)
                    && another.getTraceLocation().equalsIgnoreCase(this.traceLocation);
        }
        return false;
    }
}
