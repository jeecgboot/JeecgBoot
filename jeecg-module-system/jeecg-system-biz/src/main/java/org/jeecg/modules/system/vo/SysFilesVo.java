package org.jeecg.modules.system.vo;

import lombok.Data;

/**
 * @Description:
 * @author: wangshuai
 * @date: 2022年09月21日 17:27
 */
@Data
public class SysFilesVo {
    /**
     * 需要复制的文件夹或者文件id
     */
    private String fileId;

    /**
     * 需要复制到哪个文件夹下的id
     */
    private String copyToFileId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 委托人的用户id
     */
    private String msgTo;

    /**
     * 权限
     */
    private String authority;

    /**
     * 文件名称
     */
    private String fileName;


    /**
     * 删除状态
     */
    private String delFlag;

    /**
     * 文件大小
     */
    private Double fileSize;

    /**
     * 文件路径
     */
    private String fileUrl;

    /**
     * 说明（添加到系统日志）
     */
    private String description;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 下载数
     */
    private String downCount;

    /**
     * 阅读数
     */
    private String readCount;

    /**
     * 父id
     */
    private String parentId;

    /**
     * 分享地址
     */
    private String shareUrl;

    /**
     * 是否允许下载(1：是  0：否)
     */
    private String enableDown;

    /**
     * 分享权限(1.关闭分享 2.允许所有联系人查看 3.允许任何人查看)
     */
    private String sharePerms;

    /**
     * 是否允许修改(1：是  0：否)
     */
    private String enableUpdat;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 权限方式(enableDown:下载,enableUpdat:修改,sharePerms:分享权限,reduction:还原,rename:重命名,newFile:上传新版本)
     */
    private String type;

    /**
     * 最上级的id
     */
    private String rootId;

    /**
     * 是否为文件夹(0否 1是)
     */
    private String izFolder;

    /**
     * 是否为一级文件夹(0否 1是)
     */
    private String izRootFolder;
}
