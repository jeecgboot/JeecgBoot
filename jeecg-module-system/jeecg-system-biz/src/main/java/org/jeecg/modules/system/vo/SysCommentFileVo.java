package org.jeecg.modules.system.vo;

import lombok.Data;

/**
 * @Description: 文档VO
 * @Author: jeecg-boot
 * @Date: 2022-07-21
 * @Version: V1.0
 */
@Data
public class SysCommentFileVo {

    /**
     * sys_files id
     */
    private String fileId;
    /**
     * sys_form_file id
     */
    private String sysFormFileId;
    /**
     * 文件名称
     */
    private String name;

    private Double fileSize;

    /**
     * 文件地址
     */
    private String url;

    /**
     * 文档类型（folder:文件夹 excel:excel doc:word pp:ppt image:图片  archive:其他文档 video:视频）
     */
    private String type;

    /**
     * 文件上传类型(temp/本地上传(临时文件) manage/知识库)
     */
    private String storeType;

}
