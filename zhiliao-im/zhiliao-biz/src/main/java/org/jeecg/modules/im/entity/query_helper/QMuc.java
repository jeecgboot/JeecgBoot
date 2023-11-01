package org.jeecg.modules.im.entity.query_helper;


import org.jeecg.modules.im.entity.Muc;
import lombok.Data;

@Data
public class QMuc extends Muc {
    private String ids;
    private String userAccount;
    private String masterAccount;
    private Boolean isDefaultJoin;
    private Boolean isDelete;
    private String welcomes;
}
