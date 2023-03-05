package org.jeecg.modules.system.vo.lowapp;

import lombok.Data;

import java.util.List;

/**
 * @Author taoYan
 * @Date 2022/12/30 10:52
 **/
@Data
public class DepartInfo {

    private String id;

    /**
     * 上级名称-下级名称
     */
    private List<String> orgName;

    /**
     * 上级ID-下级ID
     */
    private List<String> orgId;

}
