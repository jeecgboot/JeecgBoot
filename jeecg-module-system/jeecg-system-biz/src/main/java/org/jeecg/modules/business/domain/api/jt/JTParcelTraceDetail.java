package org.jeecg.modules.business.domain.api.jt;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import static org.jeecg.modules.business.domain.api.ScanType.*;

@Slf4j
@Data
public class JTParcelTraceDetail {

    @JSONField(deserialize = false)
    private String parcelId;

    @JsonProperty("scantime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
    private String scanTime;

    @JsonProperty("scantype")
    private String scanType;

    @JsonProperty("desc")
    private String descriptionCn;

    @JsonProperty("descriptionEn")
    private String descriptionEn;

    public JTParcelTraceDetail(String scanTime, String scanType, String descriptionCn, String descriptionEn) {
        this.scanTime = scanTime;
        this.scanType = scanType;
        this.descriptionCn = descriptionCn;
        this.descriptionEn = descriptionEn;
    }

    public final static String PROBLEME_ADRESSAGE = "En raison d'un problème d'adressage, votre colis a été réacheminé vers le site de distribution qui dessert votre adresse.";
    public final static String PROBLEME_ADRESSAGE_CN = "您的包裹路由发生错误.它目前正在被重新路由到其交货地点.";
    public final static String ADRESSE_INCOMPLETE_INCORRECTE = "L'adresse indiquée sur votre colis est incomplète ou incorrecte. Nous procédons à sa correction afin de permettre le bon acheminement de votre colis.";
    public final static String ADRESSE_INCOMPLETE_INCORRECTE_CN = "由于送货地址问您的包裹无法送达.它将退还给发件人.";
    public final static String ADRESSE_INCOMPLETE_INCORRECTE_CN_2 = "由于收货地址不完整，您的包裹目前无法送达收件人。收件人可以通过单击在线联系我们联系我们的客户服务以提供必要的附加信息。";
    public final static String DISTRIBUTION_SUSPENDUE = "La distribution à domicile est actuellement suspendue sur votre secteur. Votre colis va être acheminé vers votre point de retrait habituel.";
    public final static String DANS_BOITE_POSTALE = "Votre colis est à votre disposition dans votre boîte postale.";
    public final static String NON_REMIS = "Votre colis n'a pas pu vous être remis.";
    public final static String COLIS_REFUSE = "Vous avez refusé votre colis car il était détérioré. Il est retourné à l'expéditeur.";
    public final static String NO_DELIVERY_TODAY = "您的包裹今天无法送达.我们将尽快交付.";
    public final static String NO_DELIVERY_TODAY_FR = "Votre colis ne peut être livré ce jour en raison d'une situation exceptionnelle indépendante de notre volonté. Il sera mis en livraison dans les prochains jours.";
    public final static String DELIVERY_NEXT_BUSINESS_DAY = "您的包裹无法送达.它将在下一个工作日交付.";

    public JTParcelTraceDetail() {
    }

    /**
     * Set parcel ID, and add missing English description
     * @param parcelId Parcel ID to which the trace belongs to
     */
    public void parcelTraceProcess(String parcelId) {
        if (scanType == null) {
            return;
        }
        setParcelId(parcelId);
        if (descriptionEn == null || descriptionEn.isEmpty()) {
            switch (scanType) {
                case "Order Placed" :
                    setDescriptionEn("The order has been officially placed by the sender.");
                    break;
                case "Received by Consolidation Warehouse" :
                    setDescriptionEn("The parcel has arrived at the consolidation warehouse.");
                    break;
                case "Parcel Weighing" :
                    setDescriptionEn("The parcel is being weighed before departure.");
                    break;
                case "Bagging" :
                    setDescriptionEn("The parcel is being bagged before leaving consolidation warehouse.");
                    break;
                case "Consolidation Center Dispatch":
                    setDescriptionEn("The parcel has been dispatched from consolidation center.");
                    break;
                case "Aviation Stowage" :
                    setDescriptionEn("The parcel is being stowed into the flight." + descriptionCn);
                    break;
                case "Flight Departure" :
                    setDescriptionEn("The flight has departed.");
                    break;
                case "Flight Arrived" :
                    setDescriptionEn("The flight has arrived at its destination.");
                    break;
                case "Customs clearance" :
                    setDescriptionEn("The parcel is being cleared at the customs.");
                    break;
                case "Customs Clearance Completed" :
                    setDescriptionEn("The parcel has completed its customs clearance.");
                    break;
            }
        } else {
            switch (scanType) {
                case "已签收":
                case "MD006":
                case DANS_BOITE_POSTALE:
                    setScanType(END_DELIVERED.getDesc());
                    break;
                case "末端收件":
                case "MD001":
                    setScanType(END_RECEIVED.getDesc());
                    break;
                case "等待签收":
                case "MD009":
                    setScanType(END_DELIVERY.getDesc());
                    break;
                case "MD007":
                    setScanType(RETURN.getDesc());
                    setDescriptionEn("The parcel will be returned to sender.");
                    break;
                case COLIS_REFUSE:
                    setScanType(RETURN.getDesc());
                    break;
                case "MD1011":
                case "1011":
                    setScanType(END_ARRIVED.getDesc());
                    setDescriptionEn("The parcel is being routed towards its final destination.");
                    break;
                case PROBLEME_ADRESSAGE:
                case PROBLEME_ADRESSAGE_CN:
                case ADRESSE_INCOMPLETE_INCORRECTE:
                case DISTRIBUTION_SUSPENDUE:
                case NON_REMIS:
                case ADRESSE_INCOMPLETE_INCORRECTE_CN:
                case ADRESSE_INCOMPLETE_INCORRECTE_CN_2:
                case NO_DELIVERY_TODAY:
                case DELIVERY_NEXT_BUSINESS_DAY:
                case NO_DELIVERY_TODAY_FR:
                    setScanType(END_ABNORMAL.getDesc());
                    break;
            }
        }
    }

    public boolean equals(Object anotherDetail) {
        if (anotherDetail instanceof JTParcelTraceDetail) {
            JTParcelTraceDetail another = (JTParcelTraceDetail) anotherDetail;
            return another.getParcelId().equals(this.parcelId)
                    && another.getScanType().equals(this.scanType)
                    && another.getScanTime().equals(this.scanTime)
                    && another.getDescriptionEn().equals(this.descriptionEn);
        }
        return false;
    }
}
