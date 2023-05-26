package org.jeecg.modules.business.domain.api.equick;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static org.jeecg.modules.business.domain.api.ScanType.*;

@Slf4j
@Data
public class EQuickTraceData {

    @JSONField(deserialize = false)
    private String parcelId;

    @JSONField(deserialize = false)
    private String scanType;

    /**
     * +
     * 产品代码
     */
    @JsonProperty("QuickType")
    private String quickType;

    /**
     * 国外标签单号
     */
    @JsonProperty("TraceLabelNo")
    private String traceLabelNo;

    /**
     * >=200 – 国外部分 >=100<=199 – 国内部分 特别关注： 299 – 妥投 298 – 派送异常
     */
    @JsonProperty("TraceKind")
    private Integer traceKind;

    /**
     * 追踪时间
     */
    @JsonProperty("TraceDateTime")
    private String traceDateTime;

    /**
     * 追踪地点
     */
    @JsonProperty("TraceLocation")
    private String traceLocation;

    /**
     * 追踪说明
     */
    @JsonProperty("TraceContent")
    private String traceContent;

    /**
     * 追踪国家
     */
    @JsonProperty("TraceCountry")
    private String traceCountry;

    /**
     * 追踪状态
     */
    @JsonProperty("TraceStatus")
    private String traceStatus;

    /**
     * 目的地单号
     */
    @JsonProperty("DestinationWBNo")
    private String destinationWBNo;

    public EQuickTraceData(String quickType, String traceLabelNo, Integer traceKind, String traceDateTime, String traceLocation, String traceContent, String traceCountry, String traceStatus, String destinationWBNo) {
        this.quickType = quickType;
        this.traceLabelNo = traceLabelNo;
        this.traceKind = traceKind;
        this.traceDateTime = traceDateTime;
        this.traceLocation = traceLocation;
        this.traceContent = traceContent;
        this.traceCountry = traceCountry;
        this.traceStatus = traceStatus;
        this.destinationWBNo = destinationWBNo;
    }

    public EQuickTraceData() {
    }

    /**
     * Set parcel ID, and add scan type info accordingly
     *
     * @param parcelId Parcel ID to which the trace belongs to
     */
    public void parcelTraceProcess(String parcelId) {
        setParcelId(parcelId);
        switch (traceKind) {
            case 111:
                setScanType(ORDER_PLACED.getDesc());
                break;
            case 112:
                setScanType(WAITING_FOR_DELIVERY.getDesc());
                break;
            case 123:
                setScanType(FACILITY_ARRIVED.getDesc());
                break;
            case 131:
                setScanType(FACILITY_OUTBOUND.getDesc());
                break;
            case 141:
                setScanType(FACILITY_DEPARTURE.getDesc());
                setTraceContent("Arrived at carrier operation center");
                break;
            case 151:
                setScanType(FLIGHT_PREPARING.getDesc());
                setTraceContent("Departed from carrier operation center");
                break;
            case 161:
                setScanType(ARRIVED_PORT.getDesc());
                break;
            case 164:
                setScanType(FLIGHT_DEPARTURE.getDesc());
                break;
            case 168:
            case 169:
                setScanType(FLIGHT_ARRIVED.getDesc());
                break;
            case 200:
            case 232:
                setScanType(CUSTOMS_CLEARANCE_COMPLETED.getDesc());
                break;
            case 210:
                setScanType(END_RECEIVED.getDesc());
                break;
            case 251:
                setScanType(END_ARRIVED.getDesc());
                break;
            case 285:
                setScanType(END_DELIVERY.getDesc());
                break;
            case 290:
                setScanType(END_ABNORMAL.getDesc());
                break;
            case 295:
            case 298:
                setScanType(RETURN.getDesc());
                break;
            case 299:
                setScanType(END_DELIVERED.getDesc());
                break;
        }
    }
}
