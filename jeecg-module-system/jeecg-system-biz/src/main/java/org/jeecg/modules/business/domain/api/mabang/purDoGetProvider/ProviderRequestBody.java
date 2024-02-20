package org.jeecg.modules.business.domain.api.mabang.purDoGetProvider;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.RequestBody;
import java.util.function.Function;

public class ProviderRequestBody implements RequestBody {

    /**
     * 供应商名称
     */
    private String provider;
    private String contactPerson;
    private String contactTel;
    /**
     * 状态:1启用2停用
     */
    private int flag = 1;
    /**
     * 分销:1是2否
     */
    private int isDistribution;
    /**
     * 采购人编号
     */
    private int buyerId;
    /**
     * 付款方式:1到付,2分期付款,3现付,4周结,5月结,6账期支付
     */
    private int paymentType;
    /**
     * 供应商类型: 1:1688, 2淘宝 3 自建供应商
     */
    private int providerType;
    private int page = 1;
    private int rowPerPage = 50;
    /**
     * 财务编码
     */
    private String financialcode;


    @Override
    public String api() {
        return "pur-do-get-provider";
    }

    @Override
    public JSONObject parameters() {
        JSONObject json = new JSONObject();
        putNonNull(json, "flag", flag);
        putNonNull(json, "provider", provider);
        putNonNull(json, "page", page);
        putNonNull(json, "rowPerPage", rowPerPage);
        return json;
    }

    void nextPage() {
        setPage(this.page + 1);
    }

    int getPage() {
        return page;
    }

    public ProviderRequestBody setProvider(String provider) {
        this.provider = provider;
        return this;
    }
    public ProviderRequestBody setFlag(int flag) {
        this.flag = flag;
        return this;
    }
    public ProviderRequestBody setPage(int page) {
        this.page = page;
        return this;
    }
    private <E> void putNonNull(JSONObject json, String key, E value) {
        if (value != null) {
            json.put(key, value);
        }
    }

    private <E, T> void putNonNull(JSONObject json, String key, E value, Function<E, T> mapper) {
        if (value != null) {
            json.put(key, mapper.apply(value));
        }
    }


}
