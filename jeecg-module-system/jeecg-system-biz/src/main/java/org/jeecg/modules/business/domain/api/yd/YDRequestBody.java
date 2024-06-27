package org.jeecg.modules.business.domain.api.yd;

public abstract class YDRequestBody {

    private final String serviceMethod;

    private final String paramsJson;

    public YDRequestBody(String serviceMethod, String paramsJson) {
        this.serviceMethod = serviceMethod;
        this.paramsJson = paramsJson;
    }

    public String getServiceMethod() {
        return serviceMethod;
    }

    public String getParamsJson() {
        return paramsJson;
    }

}