package org.jeecg.modules.business.domain.job;

import lombok.Data;

import java.util.List;

@Data
public class ChangeWareHouseParam {
    private String shop;
    private List<String> skus;
    private List<String> countries;
}
