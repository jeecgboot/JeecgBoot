package com.shop.common.core.web;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 批量修改通用参数
 * 2020-03-13 0:11
 */
public class BatchParam<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id集合
     */
    private List<Serializable> ids;
    /**
     * 批量修改多个字段
     */
    private T data;

    /**
     * 通用批量修改方法
     *
     * @param service IService
     * @param idName  id字段名称
     * @return boolean
     */
    public boolean update(IService<T> service, String idName) {
        if (data == null) return false;
        return service.update(data, new UpdateWrapper<T>().in(idName, this.getIds()));
    }

    public List<Serializable> getIds() {
        return ids;
    }

    public void setIds(List<Serializable> ids) {
        this.ids = ids;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
