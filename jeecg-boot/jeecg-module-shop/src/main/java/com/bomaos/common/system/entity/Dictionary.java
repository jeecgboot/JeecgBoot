package com.bomaos.common.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 字典
 * Created by Panyoujie on 2020-03-14 11:29:03
 */
@TableName("sys_dictionary")
public class Dictionary implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 字典id
     */
    @TableId(value = "dict_id", type = IdType.AUTO)
    private Integer dictId;
    /**
     * 字典标识
     */
    private String dictCode;
    /**
     * 字典名称
     */
    private String dictName;
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

    public Integer getDictId() {
        return dictId;
    }

    public void setDictId(Integer dictId) {
        this.dictId = dictId;
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

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
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

    @Override
    public String toString() {
        return "Dictionary{" +
                ", dictId=" + dictId +
                ", dictCode=" + dictCode +
                ", dictName=" + dictName +
                ", sortNumber=" + sortNumber +
                ", comments=" + comments +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", deleted=" + deleted +
                "}";
    }

}
