package org.jeecg.modules.system.vo;

import lombok.Data;

/**
 * @Description:
 * @author: wangshuai
 * @date: 2022年09月27日 20:56
 */
@Data
public class SysFileLogVo {
    /**
     * 文件id
     */
    private String fileId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 日志内容
     */
    private String dataContent;
    /**
     * 真实姓名
     */
    private String realname;
    /**
     * 头像
     */
    private String avatar;

    /**
     * 日志创建时间
     */
    private String createTime;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 路径
     */
    private String url;

    /**
     * 是否为文件夹
     */
    private String izFolder;
}
