package com.shop.controller;

import com.shop.common.core.annotation.OperLog;
import com.shop.common.core.web.BaseController;
import com.shop.common.core.web.JsonResult;
import com.shop.common.core.web.PageParam;
import com.shop.common.core.web.PageResult;
import com.shop.entity.OperRecord;
import com.shop.service.OperRecordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 操作日志
 * Created by Panyoujie on 2018-12-24 16:10
 */
@Controller
@RequestMapping("/sys/operRecord")
public class OperRecordController extends BaseController {
    @Autowired
    private OperRecordService operLogService;

    @RequiresPermissions("sys:oper_record:view")
    @RequestMapping()
    public String view() {
        return "system/oper-record.html";
    }

    /**
     * 分页查询操作日志
     */
    @OperLog(value = "操作日志", desc = "分页查询")
    @RequiresPermissions("sys:oper_record:view")
    @ResponseBody
    @RequestMapping("/page")
    public PageResult<OperRecord> page(HttpServletRequest request) {
        PageParam<OperRecord> pageParam = new PageParam<>(request);
        pageParam.remove("username");
        pageParam.setDefaultOrder(null, new String[]{"create_time"});
        return operLogService.listPage(pageParam);
    }

    /**
     * 查询全部操作日志
     */
    @OperLog(value = "操作日志", desc = "查询全部")
    @RequiresPermissions("sys:oper_record:view")
    @ResponseBody
    @RequestMapping("/list")
    public JsonResult list(HttpServletRequest request) {
        PageParam<OperRecord> pageParam = new PageParam<>(request);
        pageParam.remove("username");
        List<OperRecord> records = operLogService.listAll(pageParam.getNoPageParam());
        return JsonResult.ok().setData(pageParam.sortRecords(records));
    }

    /**
     * 根据id查询操作日志
     */
    @OperLog(value = "操作日志", desc = "根据id查询")
    @RequiresPermissions("sys:oper_record:view")
    @ResponseBody
    @RequestMapping("/get")
    public JsonResult get(Integer id) {
        PageParam<OperRecord> pageParam = new PageParam<>();
        pageParam.put("id", id);
        List<OperRecord> records = operLogService.listAll(pageParam.getNoPageParam());
        return JsonResult.ok().setData(pageParam.getOne(records));
    }

}
