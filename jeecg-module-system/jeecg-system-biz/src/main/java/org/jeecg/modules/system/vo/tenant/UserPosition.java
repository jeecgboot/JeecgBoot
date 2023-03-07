package org.jeecg.modules.system.vo.tenant;

import lombok.Data;

/**
 * 用户与职位信息
 * @Author taoYan
 * @Date 2023/2/17 10:10
 **/
@Data
public class UserPosition {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 职位名称
     */
    private String positionName;
}
