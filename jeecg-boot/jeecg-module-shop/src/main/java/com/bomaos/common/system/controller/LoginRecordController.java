package com.bomaos.common.system.controller;

import com.bomaos.common.core.annotation.OperLog;
import com.bomaos.common.core.web.BaseController;
import com.bomaos.common.core.web.JsonResult;
import com.bomaos.common.core.web.PageParam;
import com.bomaos.common.core.web.PageResult;
import com.bomaos.common.system.entity.LoginRecord;
import com.bomaos.common.system.service.LoginRecordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 登录日志
 * Created by Panyoujie on 2018-12-24 16:10
 */
@Controller
@RequestMapping("/sys/loginRecord")
public class LoginRecordController extends BaseController {
    @Autowired
    private LoginRecordService loginRecordService;

    @RequiresPermissions("sys:login_record:view")
    @RequestMapping()
    public String view() {
        return "system/login-record.html";
    }

    /**
     * 分页查询登录日志
     */
    @OperLog(value = "登录日志", desc = "分页查询")
    @RequiresPermissions("sys:login_record:view")
    @ResponseBody
    @RequestMapping("/page")
    public PageResult<LoginRecord> page(HttpServletRequest request) {
        PageParam<LoginRecord> pageParam = new PageParam<>(request);
        pageParam.setDefaultOrder(null, new String[]{"create_time"});
        return loginRecordService.listPage(pageParam);
    }

    /**
     * 查询全部登录日志
     */
    @OperLog(value = "登录日志", desc = "查询全部")
    @RequiresPermissions("sys:login_record:view")
    @ResponseBody
    @RequestMapping("/list")
    public JsonResult list(HttpServletRequest request) {
        PageParam<LoginRecord> pageParam = new PageParam<>(request);
        List<LoginRecord> records = loginRecordService.listAll(pageParam.getNoPageParam());
        return JsonResult.ok().setData(pageParam.sortRecords(records));
    }

    /**
     * 根据id查询登录日志
     */
    @OperLog(value = "登录日志", desc = "根据id查询")
    @RequiresPermissions("sys:login_record:view")
    @ResponseBody
    @RequestMapping("/get")
    public JsonResult get(Integer id) {
        PageParam<LoginRecord> pageParam = new PageParam<>();
        pageParam.put("id", id);
        List<LoginRecord> records = loginRecordService.listAll(pageParam.getNoPageParam());
        return JsonResult.ok().setData(pageParam.getOne(records));
    }

}
