package org.jeecg.common.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 移动端消息推送
 * @author liusq
 * @date 2025/11/12 14:11
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PushMessageDTO implements Serializable {


    private static final long serialVersionUID = 7431775881170684867L;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 推送形式：all：全推送 single：单用户推送
     */
    private String pushType;

    /**
     * 用户名usernameList
     */
    List<String> usernames;

    /**
     * 用户名idList
     */
    List<String> userIds;

    /**
     * 消息附加参数
     */
    Map<String,Object> payload;
}
