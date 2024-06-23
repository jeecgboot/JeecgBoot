package org.jeecg.common.system.vo;


/**
 * @Description: 系统文件实体类
 * @author: wangshuai
 * @date: 2022年08月11日 9:48
 */
public class SysFilesModel {
    /**主键id*/
    private String id;
    /**文件名称*/
    private String fileName;
    /**文件地址*/
    private String url;
    /**文档类型（folder:文件夹 excel:excel doc:word pp:ppt image:图片  archive:其他文档 video:视频）*/
    private String fileType;
    /**文件上传类型(temp/本地上传(临时文件) manage/知识库)*/
    private String storeType;
    /**文件大小（kb）*/
    private Double fileSize;
    /**租户id*/
    private String tenantId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public Double getFileSize() {
        return fileSize;
    }

    public void setFileSize(Double fileSize) {
        this.fileSize = fileSize;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}