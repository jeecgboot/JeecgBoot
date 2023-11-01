package org.jeecg.modules.im.entity.query_helper;


import lombok.Data;
import org.jeecg.modules.im.entity.Activity;

@Data
public class QActivity extends Activity {
    private String userSearch;
    private Boolean isDelete;
}
