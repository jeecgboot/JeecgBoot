package com.bomaos.common.core.web;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询通用返回结果
 * Created by Panyoujie on 2017-06-10 10:10
 */
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 状态码
     */
    private int code = 0;
    /**
     * 提示信息
     */
    private String msg;
    /**
     * 总数量
     */
    private long count;
    /**
     * 当前页数据
     */
    private List<T> data;

    public PageResult() {
    }

    public PageResult(List<T> rows) {
        this(rows, rows == null ? 0 : rows.size());
    }

    public PageResult(List<T> rows, long total) {
        this.count = total;
        this.data = rows;
    }

    public PageResult(int code, List<T> rows, long total) {
        this.code = code;
        this.count = total;
        this.data = rows;
    }

    public int getCode() {
        return code;
    }

    public PageResult setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public PageResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public long getCount() {
        return count;
    }

    public PageResult setCount(long count) {
        this.count = count;
        return this;
    }

    public List<T> getData() {
        return data;
    }

    public PageResult setData(List<T> data) {
        this.data = data;
        return this;
    }

}
