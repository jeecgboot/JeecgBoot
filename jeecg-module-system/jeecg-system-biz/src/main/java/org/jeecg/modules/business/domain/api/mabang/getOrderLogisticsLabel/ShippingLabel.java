package org.jeecg.modules.business.domain.api.mabang.getOrderLogisticsLabel;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ShippingLabel {
    // 地址单
    private String a;
    // 报关单
    private String c;
    // 配货单
    private String p;
    private String i;
    // 地址报关一体单
    private String ac;

    public ShippingLabel(JSONObject object) {
        try {
            this.a = object.getString("a");
            this.c = object.getString("c");
            this.p = object.getString("p");
            this.i = object.getString("i");
            this.ac = object.getString("ac");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isEmpty() {
        return (a == null || a.isEmpty()) && (ac == null || ac.isEmpty());
    }

    public String getAvailableLabelUrl() {
        return a.isEmpty() ? ac : a;
    }

    @Override
    public String toString() {
        return "ShippingLabel{" +
                "a='" + a + '\'' +
                ", c='" + c + '\'' +
                ", p='" + p + '\'' +
                ", i='" + i + '\'' +
                ", ac='" + ac + '\'' +
                '}';
    }
}
