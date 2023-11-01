package org.jeecg.modules.im.entity.query_helper;


import lombok.Data;
import org.jeecg.modules.im.entity.Friend;
import org.jeecg.modules.im.entity.SayHello;

@Data
public class QSayHello extends SayHello {
    private String fromUserSearch;
    private String toUserSearch;
    private String ts_deal;
}
