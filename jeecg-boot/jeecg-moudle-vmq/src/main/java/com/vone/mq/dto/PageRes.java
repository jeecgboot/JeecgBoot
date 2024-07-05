package com.vone.mq.dto;

public class PageRes {
    private int code;
    private String msg;
    private long count;
    private Object data;

    public static PageRes error(String remsg){
        PageRes p = new PageRes();
        p.setCode(-1);
        p.setMsg(remsg);
        p.setCount(0);
        return p;
    }
    public static PageRes success(long recount,Object obj){
        PageRes p = new PageRes();
        p.setCode(0);
        p.setMsg("成功");
        p.setCount(recount);
        p.setData(obj);
        return p;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
