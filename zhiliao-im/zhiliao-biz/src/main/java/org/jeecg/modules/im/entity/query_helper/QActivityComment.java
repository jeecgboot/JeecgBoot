package org.jeecg.modules.im.entity.query_helper;


import lombok.Data;
import org.jeecg.modules.im.entity.Activity;
import org.jeecg.modules.im.entity.ActivityComment;

@Data
public class QActivityComment extends ActivityComment {
    private String userSearch;
    private Boolean isDelete;
}
