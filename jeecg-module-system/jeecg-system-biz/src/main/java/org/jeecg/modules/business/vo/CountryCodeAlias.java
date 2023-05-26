package org.jeecg.modules.business.vo;

import lombok.Data;

@Data
public class CountryCodeAlias {
    private final String realName;
    private final String alias;

    public CountryCodeAlias(String realName, String alias) {
        this.realName = realName;
        this.alias = alias;
    }
}
