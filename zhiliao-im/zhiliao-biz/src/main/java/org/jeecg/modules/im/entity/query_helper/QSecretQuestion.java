package org.jeecg.modules.im.entity.query_helper;


import lombok.Data;
import org.jeecg.modules.im.entity.Link;
import org.jeecg.modules.im.entity.SecretQuestion;

@Data
public class QSecretQuestion extends SecretQuestion {
    private String keyword;
}
