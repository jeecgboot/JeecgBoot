package com.exam.viewmodel.admin.file;



import java.util.List;


public class UeditorConfigVM {
    private String imageActionName;
    private  String imageFieldName;
    private Long imageMaxSize;
    private List<String> imageAllowFiles;
    private boolean imageCompressEnable;
    private Integer imageCompressBorder;
    private String imageInsertAlign;
    private String imageUrlPrefix;
    private String imagePathFormat;

    public String getImageActionName() {
        return imageActionName;
    }

    public void setImageActionName(String imageActionName) {
        this.imageActionName = imageActionName;
    }

    public String getImageFieldName() {
        return imageFieldName;
    }

    public void setImageFieldName(String imageFieldName) {
        this.imageFieldName = imageFieldName;
    }

    public Long getImageMaxSize() {
        return imageMaxSize;
    }

    public void setImageMaxSize(Long imageMaxSize) {
        this.imageMaxSize = imageMaxSize;
    }

    public List<String> getImageAllowFiles() {
        return imageAllowFiles;
    }

    public void setImageAllowFiles(List<String> imageAllowFiles) {
        this.imageAllowFiles = imageAllowFiles;
    }

    public boolean isImageCompressEnable() {
        return imageCompressEnable;
    }

    public void setImageCompressEnable(boolean imageCompressEnable) {
        this.imageCompressEnable = imageCompressEnable;
    }

    public Integer getImageCompressBorder() {
        return imageCompressBorder;
    }

    public void setImageCompressBorder(Integer imageCompressBorder) {
        this.imageCompressBorder = imageCompressBorder;
    }

    public String getImageInsertAlign() {
        return imageInsertAlign;
    }

    public void setImageInsertAlign(String imageInsertAlign) {
        this.imageInsertAlign = imageInsertAlign;
    }

    public String getImageUrlPrefix() {
        return imageUrlPrefix;
    }

    public void setImageUrlPrefix(String imageUrlPrefix) {
        this.imageUrlPrefix = imageUrlPrefix;
    }

    public String getImagePathFormat() {
        return imagePathFormat;
    }

    public void setImagePathFormat(String imagePathFormat) {
        this.imagePathFormat = imagePathFormat;
    }
}
