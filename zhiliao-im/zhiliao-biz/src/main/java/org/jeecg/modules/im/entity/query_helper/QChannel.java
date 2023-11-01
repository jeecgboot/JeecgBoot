package org.jeecg.modules.im.entity.query_helper;


import lombok.Data;
import org.jeecg.modules.im.entity.BlockIp;
import org.jeecg.modules.im.entity.Channel;

@Data
public class QChannel extends Channel {
    private String userAccount;
    private String mucName;
}
