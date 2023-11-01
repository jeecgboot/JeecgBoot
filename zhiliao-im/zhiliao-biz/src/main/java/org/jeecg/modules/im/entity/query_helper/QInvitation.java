package org.jeecg.modules.im.entity.query_helper;

import lombok.Data;
import org.jeecg.modules.im.entity.Invitation;
import org.jeecg.modules.im.entity.InviteCode;

@Data
public class QInvitation extends Invitation {
    private String inviterAccount;
    private String userAccount;
}
