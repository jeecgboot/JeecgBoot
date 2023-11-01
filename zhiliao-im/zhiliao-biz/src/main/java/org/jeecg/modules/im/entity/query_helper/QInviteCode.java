package org.jeecg.modules.im.entity.query_helper;

import lombok.Data;
import org.jeecg.modules.im.entity.InviteCode;

@Data
public class QInviteCode extends InviteCode {
    private String userAccount;
}
