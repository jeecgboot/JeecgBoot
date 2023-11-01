package org.jeecg.modules.im.entity.query_helper;


import lombok.Data;
import org.jeecg.modules.im.entity.RedPackOpen;

@Data
public class QRedPackOpen extends RedPackOpen {
    private String useSearch;
    private Integer senderId;
}
