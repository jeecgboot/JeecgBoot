package org.jeecg.modules.im.entity.query_helper;

import org.jeecg.modules.im.entity.MucMemberLevel;
import org.jeecg.modules.im.entity.MucMemberLevelCtg;
import lombok.Data;

@Data
public class QMucMemberLevel extends MucMemberLevel {

    private MucMemberLevelCtg memberLevelCtg;

}
