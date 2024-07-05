package com.vone.mq.utils;


import com.vone.mq.dto.CommonRes;

public class ResUtil {

    public static CommonRes success(Object o){
        CommonRes commonRes = new CommonRes();
        commonRes.setCode(1);
        commonRes.setMsg("成功");
        commonRes.setData(o);
        return commonRes;
    }

    public static CommonRes success(){
        CommonRes commonRes = new CommonRes();
        commonRes.setCode(1);
        commonRes.setMsg("成功");
        return commonRes;
    }

    public static CommonRes error(String msg){
        CommonRes commonRes = new CommonRes();
        commonRes.setCode(-1);
        commonRes.setMsg(msg);
        return commonRes;
    }

    public static CommonRes error(int code,Object data){
        CommonRes commonRes = new CommonRes();
        commonRes.setCode(code);
        commonRes.setMsg("失败");
        commonRes.setData(data);
        return commonRes;
    }

    public static CommonRes error(){
        CommonRes commonRes = new CommonRes();
        commonRes.setCode(-1);
        commonRes.setMsg("失败");
        return commonRes;
    }
}
