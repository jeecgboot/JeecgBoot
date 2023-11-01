package org.jeecg.modules.im.entity.query_helper;


import lombok.Data;
import org.jeecg.modules.im.entity.UserNickname;

@Data
public class QUserNickname extends UserNickname {
    private String userSearch;
}
