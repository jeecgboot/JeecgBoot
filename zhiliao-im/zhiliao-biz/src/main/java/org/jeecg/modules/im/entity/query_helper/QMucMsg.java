package org.jeecg.modules.im.entity.query_helper;


import org.jeecg.modules.im.entity.MucMsg;
import lombok.Data;

@Data
public class QMucMsg extends MucMsg {
    private Boolean isDelete;//已逻辑删除
    private Boolean isTop;//已置顶
    private Boolean after = false;//指定时间戳之后
    private String mucIds;
    private long sendStartTime;
    private long sendEndTime;
    private int pageSize;
    private String s;//发送方
    private String r;//接收方
}
