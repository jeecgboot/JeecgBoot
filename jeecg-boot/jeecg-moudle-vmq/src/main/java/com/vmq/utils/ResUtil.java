package com.vmq.utils;


import com.vmq.constant.Constant;
import com.vmq.dto.CommonRes;

public class ResUtil {

    public static CommonRes success(Object o){
        CommonRes commonRes = new CommonRes();
        commonRes.setCode(1);
        commonRes.setMsg(Constant.SUCCESS);
        commonRes.setData(o);
        return commonRes;
    }

    public static CommonRes success(){
        CommonRes commonRes = new CommonRes();
        commonRes.setCode(1);
        commonRes.setMsg(Constant.SUCCESS);
        return commonRes;
    }

    public static CommonRes success(String msg){
        CommonRes commonRes = new CommonRes();
        commonRes.setCode(1);
        commonRes.setMsg(msg);
        commonRes.setData(msg);
        return commonRes;
    }

    public static CommonRes error(String msg){
        CommonRes commonRes = new CommonRes();
        commonRes.setCode(-1);
        commonRes.setMsg(msg);
        commonRes.setData(msg);
        return commonRes;
    }

    public static CommonRes error(int code,Object data){
        CommonRes commonRes = new CommonRes();
        commonRes.setCode(code);
        commonRes.setMsg(Constant.ERROR);
        commonRes.setData(data);
        return commonRes;
    }

    public static CommonRes error(){
        CommonRes commonRes = new CommonRes();
        commonRes.setCode(-1);
        commonRes.setMsg(Constant.ERROR);
        return commonRes;
    }
}
