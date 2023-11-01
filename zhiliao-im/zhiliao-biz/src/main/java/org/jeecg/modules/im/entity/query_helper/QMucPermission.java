package org.jeecg.modules.im.entity.query_helper;


import lombok.Data;
import org.jeecg.modules.im.entity.MucPermission;
import org.jeecg.modules.im.entity.User;

@Data
public class QMucPermission extends MucPermission {
    private String title;
    private Integer update;
}
