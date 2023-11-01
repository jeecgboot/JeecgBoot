package org.jeecg.modules.im.entity.query_helper;


import org.jeecg.modules.im.entity.Sticker;
import lombok.Data;

@Data
public class QSticker extends Sticker {
    private String userAccount;
    private String search;
    private Integer senderId;
}
