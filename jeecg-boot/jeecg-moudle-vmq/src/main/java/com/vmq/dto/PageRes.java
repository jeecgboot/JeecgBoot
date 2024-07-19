package com.vmq.dto;

import lombok.Data;

@Data
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
}
