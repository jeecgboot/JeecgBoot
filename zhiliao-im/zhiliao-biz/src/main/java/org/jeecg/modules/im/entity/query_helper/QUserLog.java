package org.jeecg.modules.im.entity.query_helper;


import org.jeecg.modules.im.entity.UserLog;
import lombok.Data;

@Data
public class QUserLog extends UserLog {
    private String userSearch;
    private String deviceSearch;
}
