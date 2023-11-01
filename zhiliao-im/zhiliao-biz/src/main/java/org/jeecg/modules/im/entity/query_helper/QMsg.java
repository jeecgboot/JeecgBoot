package org.jeecg.modules.im.entity.query_helper;


import org.jeecg.modules.im.entity.Msg;
import lombok.Data;

@Data
public class QMsg extends Msg {
    private Boolean isDelete;//已逻辑删除
    private Boolean isTop;//已置顶
    private Boolean after = false;//指定时间戳之后的消息
    private long sendStartTime;
    private long sendEndTime;
    private int pageSize;
    private String sender;
    private String receiver;
}
