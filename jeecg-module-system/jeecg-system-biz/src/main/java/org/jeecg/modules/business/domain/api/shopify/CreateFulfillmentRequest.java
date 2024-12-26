package org.jeecg.modules.business.domain.api.shopify;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

@Slf4j
public class CreateFulfillmentRequest extends ShopifyRequest {

    private final static String AFTERSHIP = "https://www.aftership.com/en/track/%s";
    private final static String OTHER = "Other";

    private enum TransportCompany {
        // 12 [0-9]
        CJ_LOGISTICS("https://www.cjlogistics.com/ko/tool/parcel/tracking", "CJ대한통운", "57575[0-9]{7}|58476[0-9]{7}|5901[0-9]{8}|57476[0-9]{7}"),
        // 14 [0-9]
        DPD_BE("https://www.dpdgroup.com/be/mydpd/my-parcels/track?parcelNumber=%s", "DPD", "06086316[0-9]{6}"),
        DPD_DE("https://www.dpd.com/de/de/", "DPD", "0150534[0-9]{7}"),
        DPD_AT("https://www.mydpd.at", "DPD", "06215167[0-9]{6}"),
        GLS_NL_2("https://www.gls-info.nl/tracking", "GLS", "(1437|1000)[0-9]{10}"),
        DPD_CH("https://www.dpdgroup.com/ch/mydpd/my-parcels/incoming?parcelNumber=%s", "DPD", "06086328[0-9]{6}"),
        // 16 [0-9]
        CANADA_POST("https://www.canadapost-postescanada.ca/track-reperage/en#/search?searchFor=%s", "Canada Post", "(201255|732131|102303)[0-9]{10}"),
        // 18 [0-9]
        QUICKPAC("https://quickpac.ch/de/tracking", "Quickpac", "44001091[0-9]{10}"),
        ECO_SCOOTING("https://www.ecoscooting.com/tracking/%s", "EcoScooting", "[0-9]{5}00000[0-9]{8}"),
        // 19 [0-9]
        EARLY_BIRD("https://earlybird.se/", "Early Bird", "[0-9]{19}"),
        // 21 [0-9]
        DAO("https://www.dao.as/privat/find-din-pakke?stregkode=%s", "DAO", "00057151270[0-9]{10}"),
        // 20 [0-9]
        DHL_PACKET("https://www.dhl.de/en/privatkunden/pakete-empfangen/verfolgen.html?piececode=%s", "DHL Packet", "0034[0-9]{16}"),
        GLS_NL("https://www.gls-info.nl/tracking", "GLS", "[0-9]{20}"),
        // 22 [0-9]
        CTT_EXPRESS("https://www.cttexpress.com/localizador-de-envios/", "CTT Express", "0082800082909[0-9]{9}"),
        // 24 [0-9]
        AUSTRIAN_POST("https://www.post.at/s/sendungsdetails?snr=%s", "Austrian Post", "15828030053[0-9]{13}"),
        // 26/35 [0-9]
        USPS("https://tools.usps.com/go/TrackConfirmAction?tRef=fullpage&tLc=2&text28777=&tLabels=%s&tABt=false", "USPS", "926[0-9]{23}|420[0-9]{5}926[0-9]{23}"),
        LA_POSTE("https://www.laposte.fr/outils/suivre-vos-envois?code=%s", "La Poste", "[69][A-Z]{1}[0-9]{11}|LP[0-9]{9}FR"),
        SWISS_POST("https://service.post.ch/ekp-web/ui/entry/search/%s", "Swiss Post", "[0-9]{18}|LW[0-9]{9}CH"),
        DHL_PACKET_WIA("https://www.dhl.de/en/privatkunden/pakete-empfangen/verfolgen.html?piececode=%s", "DHL Packet", "CD[0-9]{9}DE"),
        DHL_PARCEL_WIA_NL("https://my.dhlparcel.nl/home/tracktrace/%s/%s?lang=nl_NL", "DHL Parcel", "3S[A-Z]{4}[0-9]{9}"),
        DHL_PARCEL_NL("https://my.dhlecommerce.nl/home/tracktrace/%s/%s?lang=nl_NL", "DHL Parcel", "JVGL[0-9]{20}"),
        GLS_IT("https://gls-group.com/IT/it/servizi-online/ricerca-spedizioni.html?match=%s&type=NAT", "GLS", "LT[0-9]{9}"),
        COLIS_PRIVE_BE("https://colisprive.com/moncolis/pages/detailColis.aspx?numColis=%s&lang=fr", "Colis Privé", "Q[0-9]{11}[B][0-9]{4}|801000[0-9]{6}|R6700035[0-9]{4}B[0-9]{4}"),
        COLIS_PRIVE_LU("https://colisprive.com/moncolis/pages/detailColis.aspx?numColis=%s", "Colis Privé", "Q[0-9]{11}[L][0-9]{4}"),
        COLIS_PRIVE_BE_WIA("https://colisprive.com/moncolis/pages/detailColis.aspx?numColis=%sB%s", "Colis Privé", "LC[0-9]{10}"),
        COLIS_PRIVE_FR("https://colisprive.com/moncolis/pages/detailColis.aspx?numColis=%s", "Colis Privé", "(D|Q)[0-9]{16}"),
        POSTI("https://www.posti.fi/fi/seuranta#/lahetys/%s", "Posti", "SP[0-9]{9}FI"),
        POSTE_ITALIANE("https://www.poste.it/cerca/index.html#/risultati-spedizioni/%s", "Poste Italiane", "5P[0-9]{2}[A-Z][0-9]{8}"),
        FASTWAY("https://www.fastway.ie/courier-services/track-your-parcel/?l=%s", "Fastway", "3H0001[0-9]{6}"),
        HERMES_DE("https://www.myhermes.de/empfangen/sendungsverfolgung/sendungsinformation#%s", "Hermes", "H100081[0-9]{13}"),
        HERMES_GB("https://www.hermesworld.com/en/our-services/distribution/parcel-delivery/parcel-tracking/?trackingNo=%s", "Hermes", "T00D7A[0-9]{10}"),
        EVRI("https://www.evri.com/track/parcel/%s/details", "Evri", "H[0-9A-Z]{5}[0-9]{10}"),
        YODEL("https://www.yodel.co.uk/tracking/%s/%s", "Yodel", "JD[0-9]{16}"),
        UK_ROYAL_MAIL("https://www.royalmail.com/track-your-item#/tracking-results/%s", "Royal Mail", "(FJ002|WB788)[0-9]{6}GB"),
        POST_NL("https://postnl.post/", "PostNL International Mail", "LS[0-9]{9}NL"),
        COLI_COLI("https://www.colicoli.fr/trackings?id=%s", "Coli Coli", "CC[0-9]{14}[A-Z]*"),
        LUXEMBOURG_POST("https://www.post.lu/particuliers/colis-courrier/track-and-trace#/search", "Luxembourg Post", "LL[0-9]{9}LU"),
        PDN_Express("https://pdn.express/nl/track/%s", "PDN Express", "PDN0003[0-9]{6}"),
        UNI_EXPRESS("https://www.uniuni.com/tracking/#tracking-detail?no=%s", "Uni Express", "GV24CA[0-9A-Z]{12}"),
        ;

        private final String trackingUrl;
        private final String name;
        private final String regex;

        TransportCompany(String trackingUrl, String name, String regex) {
            this.trackingUrl = trackingUrl;
            this.name = name;
            this.regex = regex;
        }
    }

    public CreateFulfillmentRequest(ShopifyRequestBody body) {
        super(HttpMethod.POST, body);
    }

    @Override
    protected JSONObject generateJson(ShopifyRequestBody body) {
        JSONObject json = new JSONObject();
        JSONObject fulfillment = new JSONObject();
        fulfillment.put("notify_customer", true);

        JSONObject trackingInfo = new JSONObject();
        String trackingNumber = ((CreateFulfillmentRequestBody) body).getTrackingNumber();
        String postcode = ((CreateFulfillmentRequestBody) body).getPostcode();
        trackingInfo.put("number", trackingNumber);
        TransportCompany transportCompany = resolveTransportCompany(trackingNumber);
        String trackingUrl = transportCompany == null ? AFTERSHIP : transportCompany.trackingUrl;
        trackingInfo.put("url", String.format(trackingUrl, trackingNumber, postcode.replace(" ", "")));
        String trackingCompanyName = transportCompany == null ? OTHER : transportCompany.name;
        trackingInfo.put("company", String.format(trackingCompanyName, trackingNumber));
        fulfillment.put("tracking_info", trackingInfo);

        JSONArray lineItems = new JSONArray();
        JSONObject fulfillmentOrderId = new JSONObject();
        fulfillmentOrderId.put("fulfillment_order_id", ((CreateFulfillmentRequestBody) body).getFulfillmentId());
        lineItems.add(fulfillmentOrderId);

        fulfillment.put("line_items_by_fulfillment_order", lineItems);
        json.put("fulfillment", fulfillment);
        return json;
    }

    private TransportCompany resolveTransportCompany(String trackingNumber) {
        for (TransportCompany transportCompany : TransportCompany.values()) {
            if (trackingNumber.matches(transportCompany.regex)) {
                log.info("{} matches {}'s regex", trackingNumber, transportCompany.name);
                return transportCompany;
            }
        }
        log.info("{} couldn't be matched to any transporter, generic Aftership URL will be given.", trackingNumber);
        return null;
    }

    protected HttpHeaders buildHeaders() {
        HttpHeaders headers = super.buildHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        return headers;
    }
}
