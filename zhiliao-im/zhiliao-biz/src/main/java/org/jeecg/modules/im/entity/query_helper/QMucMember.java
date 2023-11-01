package org.jeecg.modules.im.entity.query_helper;


import org.jeecg.modules.im.entity.MucMember;
import lombok.Data;

@Data
public class QMucMember extends MucMember {
    private String userAccount;
    //查询僵尸号
    private Boolean isRobot;
    private String mucIds;
}
