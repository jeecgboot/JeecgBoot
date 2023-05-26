package org.jeecg.modules.business.entity;

import lombok.Data;

@Data
public class Country {
    private final String id;
    private final String nameEn;
    private final String nameZh;
    private final String code;
    private final String specialName;

    public Country(String id, String nameEn, String nameZh, String code, String specialName) {
        this.id = id;
        this.nameEn = nameEn;
        this.nameZh = nameZh;
        this.code = code;
        this.specialName = specialName;
    }
}
