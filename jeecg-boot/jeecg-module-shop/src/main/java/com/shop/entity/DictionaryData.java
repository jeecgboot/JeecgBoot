package com.shop.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 字典项
 * Created by Panyoujie on 2020-03-14 11:29:04
 */
@TableName("sys_dictionary_data")
public class DictionaryData implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 字典项id
     */
    @TableId(value = "dict_data_id", type = IdType.AUTO)
    private Integer dictDataId;
    /**
     * 字典id
     */
    private Integer dictId;
    /**
     * 字典项标识
     */
    private String dictDataCode;
    /**
     * 字典项名称
     */
    private String dictDataName;
    /**
     * 排序号
     */
    private Integer sortNumber;
    /**
     * 备注
     */
    private String comments;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 是否删除,0否,1是
     */
    @TableLogic
    private Integer deleted;
    /**
     * 字典代码
     */
    @TableField(exist = false)
    private String dictCode;
    /**
     * 字典名称
     */
    @TableField(exist = false)
    private String dictName;

    public Integer getDictDataId() {
        return dictDataId;
    }

    public void setDictDataId(Integer dictDataId) {
        this.dictDataId = dictDataId;
    }

    public Integer getDictId() {
        return dictId;
    }

    public void setDictId(Integer dictId) {
        this.dictId = dictId;
    }

    public String getDictDataCode() {
        return dictDataCode;
    }

    public void setDictDataCode(String dictDataCode) {
        this.dictDataCode = dictDataCode;
    }

    public String getDictDataName() {
        return dictDataName;
    }

    public void setDictDataName(String dictDataName) {
        this.dictDataName = dictDataName;
    }

    public Integer getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Integer sortNumber) {
        this.sortNumber = sortNumber;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    @Override
    public String toString() {
        return "DictionaryData{" +
                ", dictDataId=" + dictDataId +
                ", dictId=" + dictId +
                ", dictDataCode=" + dictDataCode +
                ", dictDataName=" + dictDataName +
                ", sortNumber=" + sortNumber +
                ", comments=" + comments +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", deleted=" + deleted +
                ", dictCode=" + dictCode +
                ", dictName=" + dictName +
                "}";
    }

}
