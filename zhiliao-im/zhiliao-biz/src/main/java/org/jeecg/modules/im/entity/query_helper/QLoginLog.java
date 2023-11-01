package org.jeecg.modules.im.entity.query_helper;


import org.jeecg.modules.im.entity.LoginLog;
import lombok.Data;

@Data
public class QLoginLog extends LoginLog {
    private String userSearch;
    private String deviceSearch;
}
