package org.jeecg.modules.im.entity.query_helper;


import org.jeecg.modules.im.entity.User;
import lombok.Data;

import java.util.Date;

@Data
public class QUser extends User {
    private Integer mucId;
    private String ids;
    //不包含的id
    private String excludeIds;
    private String excludeTypes;
    private long regStartTime;
    private long regEndTime;
    private String country;
    private String countryCode;
    private String countryDialCode;
    private String province;
    private String city;
    private String district;
    private String address;
    private String gender;
    private Date birthday;
    private String signature;
    private String email;
    private String userSearch;
}
