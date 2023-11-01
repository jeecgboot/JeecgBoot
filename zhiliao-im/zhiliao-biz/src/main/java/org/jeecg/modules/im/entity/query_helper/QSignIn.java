package org.jeecg.modules.im.entity.query_helper;


import org.jeecg.modules.im.entity.SignIn;
import lombok.Data;

@Data
public class QSignIn extends SignIn {
    private String userSearch;
}
