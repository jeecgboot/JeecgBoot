package org.jeecg.modules.im.service.base;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.constant.ConstantWeb;
import org.jeecg.modules.im.base.util.IPUtil;
import org.jeecg.modules.im.entity.Device;
import org.jeecg.modules.im.service.ParamService;
import org.jeecg.modules.im.service.UserService;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Controller
@Data
public class BaseController {
    @Value("${spring.profiles.active}")
    private String evn;
    @Autowired
    protected HttpServletRequest request;
    @Resource
    private ParamService paramService;
    @Resource
    private UserService userService;

    private int page;
    private int pageSize;
    private String column;
    private String order;

    //公共参数
    @ModelAttribute
    public void init(
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "10") int pageSize
            ) {
        setPage(pageNo);
        setPageSize(pageSize);
    }

    protected HttpSession getSession() {
        return request.getSession();
    }

    protected void setSessionAttr(String name, Object object) {
        getSession().setAttribute(name, object);
    }

    protected Object getSessionAttr(String name) {
        return getSession().getAttribute(name);
    }

    protected void setAttr(String key, Object obj) {
        request.setAttribute(key, obj);
    }

    protected String getPara(String name) {
        return request.getParameter(name);
    }

    protected String getIp(){
        String ip = request.getHeader(ConstantWeb.IP);
        if(StringUtils.isBlank(ip)){
            ip = IPUtil.getIpAddr(request);
        }
        return ip;
    }
    protected String getDeviceNo(){
        return request.getHeader(ConstantWeb.DEVICE_NO);
    }



    protected Integer getParaToInt(String name) {
        try {
            return Integer.parseInt(getPara(name));
        } catch (Exception e) {
            return null;
        }
    }

    protected Integer getParaToInt(String name, Integer defaultVal) {
        try {
            return Integer.parseInt(getPara(name));
        } catch (Exception e) {
            return defaultVal;
        }
    }

    protected Long getParaToLong(String name) {
        try {
            return Long.parseLong(getPara(name));
        } catch (Exception e) {
            return null;
        }
    }

    protected Boolean getParaToBool(String name) {
        String value = getPara(name);
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return Boolean.valueOf(getPara(name));
    }

    protected Boolean getParaToBool(String name, boolean defaultValue) {
        Object value = getPara(name);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.valueOf(getPara(name));
    }

    protected Double getParaToDouble(String name) {
        return Double.parseDouble(getPara(name));
    }

    protected String getPara(String name, String defaultValue) {
        String result = getPara(name);
        return result != null && !"".equals(result) ? result : defaultValue;
    }

    public BaseController keepPara() {
        Map<String, String[]> map = this.request.getParameterMap();
        for (Object o : map.entrySet()) {
            Map.Entry e = (Map.Entry) o;
            String[] values = (String[]) e.getValue();
            if (values.length == 1) {
                this.setAttr((String) e.getKey(), values[0]);
            } else {
                this.setAttr((String) e.getKey(), values);
            }
        }
        return this;
    }

    public Boolean setContainsElementInOtherSet(Set<Integer> set1, Set<Integer> set2) {
        Iterator<Integer> it = set1.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            if (set2.contains(obj)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public Boolean equals(String str1, String str2) {
        return StringUtils.equals(str1, str2);
    }

    public Boolean equals(Integer i1, Integer i2) {
        return i1 - i2 == 0;
    }

    public Boolean equalsIgnoreCase(String str1, String str2) {
        return StringUtils.equalsIgnoreCase(str1, str2);
    }

    public Boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }

    public Boolean isNotEmpty(String str) {
        return StringUtils.isNotEmpty(str);
    }

    protected Result<Object> fail() {
        return Result.error("");
    }

    public Result<Object> fail(String msg) {
        return Result.error(msg);
    }

    public Result<Object> fail(Object data) {
        return Result.error("error",data);
    }


    protected Result<Object> success() {
        return Result.OK();
    }

    protected Result<Object> success(Object obj) {
        return Result.OK(obj);
    }


    /**
     * 判断项目为测试环境
     * @return
     */
    protected Boolean isTest() {
        return StringUtils.equals(evn.split(",")[evn.split(",").length-1],"test");
    }
    //获取当前时间戳
    protected Long getTs(){
        return new Date().getTime();
    }

}
