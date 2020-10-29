package com.crj.java.task.front.modules.demo.test.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crj.java.task.front.common.api.vo.Result;
import com.crj.java.task.front.common.system.query.QueryGenerator;
import com.crj.java.task.front.modules.demo.test.entity.TaskOrderCustomer;
import com.crj.java.task.front.modules.demo.test.entity.TaskOrderMain;
import com.crj.java.task.front.modules.demo.test.entity.TaskOrderTicket;
import com.crj.java.task.front.modules.demo.test.service.ITaskOrderCustomerService;
import com.crj.java.task.front.modules.demo.test.service.ITaskOrderMainService;
import com.crj.java.task.front.modules.demo.test.service.ITaskOrderTicketService;
import com.crj.java.task.front.modules.demo.test.vo.TaskOrderMainPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @Description: 一对多示例（ERP TAB风格）
 * @Author: Crj
 * @Date: 2019-02-20
 * @Version: v2.0
 */
@Slf4j
@RestController
@RequestMapping("/test/order")
public class TaskOrderTabMainController {

    @Autowired
    private ITaskOrderMainService taskOrderMainService;
    @Autowired
    private ITaskOrderCustomerService taskOrderCustomerService;
    @Autowired
    private ITaskOrderTicketService taskOrderTicketService;

    /**
     * 分页列表查询
     *
     * @param taskOrderMain
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/orderList")
    public Result<?> respondePagedData(TaskOrderMain taskOrderMain,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req) {
        QueryWrapper<TaskOrderMain> queryWrapper = QueryGenerator.initQueryWrapper(taskOrderMain, req.getParameterMap());
        Page<TaskOrderMain> page = new Page<TaskOrderMain>(pageNo, pageSize);
        IPage<TaskOrderMain> pageList = taskOrderMainService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param taskOrderMainPage
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TaskOrderMainPage taskOrderMainPage) {
        TaskOrderMain taskOrderMain = new TaskOrderMain();
        BeanUtils.copyProperties(taskOrderMainPage, taskOrderMain);
        taskOrderMainService.save(taskOrderMain);
        return Result.ok("添加成功!");
    }

    /**
     * 编辑
     *
     * @param taskOrderMainPage
     * @return
     */
    @PutMapping("/edit")
    public Result<?> edit(@RequestBody TaskOrderMainPage taskOrderMainPage) {
        TaskOrderMain taskOrderMain = new TaskOrderMain();
        BeanUtils.copyProperties(taskOrderMainPage, taskOrderMain);
        taskOrderMainService.updateById(taskOrderMain);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        taskOrderMainService.delMain(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.taskOrderMainService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TaskOrderMain taskOrderMain = taskOrderMainService.getById(id);
        return Result.ok(taskOrderMain);
    }


    /**
     * 通过id查询
     *
     * @param taskOrderCustomer
     * @return
     */
    @GetMapping(value = "/listOrderCustomerByMainId")
    public Result<?> queryOrderCustomerListByMainId(TaskOrderCustomer taskOrderCustomer,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<TaskOrderCustomer> queryWrapper = QueryGenerator.initQueryWrapper(taskOrderCustomer, req.getParameterMap());
        Page<TaskOrderCustomer> page = new Page<TaskOrderCustomer>(pageNo, pageSize);
        IPage<TaskOrderCustomer> pageList = taskOrderCustomerService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 通过id查询
     *
     * @param taskOrderTicket
     * @return
     */
    @GetMapping(value = "/listOrderTicketByMainId")
    public Result<?> queryOrderTicketListByMainId(TaskOrderTicket taskOrderTicket,
                                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                  HttpServletRequest req) {
        QueryWrapper<TaskOrderTicket> queryWrapper = QueryGenerator.initQueryWrapper(taskOrderTicket, req.getParameterMap());
        Page<TaskOrderTicket> page = new Page<TaskOrderTicket>(pageNo, pageSize);
        IPage<TaskOrderTicket> pageList = taskOrderTicketService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param taskOrderCustomer
     * @return
     */
    @PostMapping(value = "/addCustomer")
    public Result<?> addCustomer(@RequestBody TaskOrderCustomer taskOrderCustomer) {
        taskOrderCustomerService.save(taskOrderCustomer);
        return Result.ok("添加成功!");
    }

    /**
     * 编辑
     *
     * @param taskOrderCustomer
     * @return
     */
    @PutMapping("/editCustomer")
    public Result<?> editCustomer(@RequestBody TaskOrderCustomer taskOrderCustomer) {
        taskOrderCustomerService.updateById(taskOrderCustomer);
        return Result.ok("添加成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/deleteCustomer")
    public Result<?> deleteCustomer(@RequestParam(name = "id", required = true) String id) {
        taskOrderCustomerService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatchCustomer")
    public Result<?> deleteBatchCustomer(@RequestParam(name = "ids", required = true) String ids) {
        this.taskOrderCustomerService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 添加
     *
     * @param taskOrderTicket
     * @return
     */
    @PostMapping(value = "/addTicket")
    public Result<?> addTicket(@RequestBody TaskOrderTicket taskOrderTicket) {
        taskOrderTicketService.save(taskOrderTicket);
        return Result.ok("添加成功!");
    }

    /**
     * 编辑
     *
     * @param taskOrderTicket
     * @return
     */
    @PutMapping("/editTicket")
    public Result<?> editTicket(@RequestBody TaskOrderTicket taskOrderTicket) {
        taskOrderTicketService.updateById(taskOrderTicket);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/deleteTicket")
    public Result<?> deleteTicket(@RequestParam(name = "id", required = true) String id) {
        taskOrderTicketService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatchTicket")
    public Result<?> deleteBatchTicket(@RequestParam(name = "ids", required = true) String ids) {
        this.taskOrderTicketService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

}
