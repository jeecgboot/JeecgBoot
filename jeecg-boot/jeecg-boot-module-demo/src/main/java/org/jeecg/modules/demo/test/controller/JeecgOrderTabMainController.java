package org.jeecg.modules.demo.test.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.demo.test.entity.JeecgOrderCustomer;
import org.jeecg.modules.demo.test.entity.JeecgOrderMain;
import org.jeecg.modules.demo.test.entity.JeecgOrderTicket;
import org.jeecg.modules.demo.test.service.IJeecgOrderCustomerService;
import org.jeecg.modules.demo.test.service.IJeecgOrderMainService;
import org.jeecg.modules.demo.test.service.IJeecgOrderTicketService;
import org.jeecg.modules.demo.test.vo.JeecgOrderMainPage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 一对多示例（ERP TAB风格）
 * @Author: ZhiLin
 * @Date: 2019-02-20
 * @Version: v2.0
 */
@Slf4j
@RestController
@RequestMapping("/test/order")
public class JeecgOrderTabMainController {

    @Autowired
    private IJeecgOrderMainService jeecgOrderMainService;
    @Autowired
    private IJeecgOrderCustomerService jeecgOrderCustomerService;
    @Autowired
    private IJeecgOrderTicketService jeecgOrderTicketService;

    /**
     * 分页列表查询
     *
     * @param jeecgOrderMain
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/orderList")
    public Result<?> respondePagedData(JeecgOrderMain jeecgOrderMain,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req) {
        QueryWrapper<JeecgOrderMain> queryWrapper = QueryGenerator.initQueryWrapper(jeecgOrderMain, req.getParameterMap());
        Page<JeecgOrderMain> page = new Page<JeecgOrderMain>(pageNo, pageSize);
        IPage<JeecgOrderMain> pageList = jeecgOrderMainService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param jeecgOrderMainPage
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody JeecgOrderMainPage jeecgOrderMainPage) {
        JeecgOrderMain jeecgOrderMain = new JeecgOrderMain();
        BeanUtils.copyProperties(jeecgOrderMainPage, jeecgOrderMain);
        jeecgOrderMainService.save(jeecgOrderMain);
        return Result.ok("添加成功!");
    }

    /**
     * 编辑
     *
     * @param jeecgOrderMainPage
     * @return
     */
    @PutMapping("/edit")
    public Result<?> edit(@RequestBody JeecgOrderMainPage jeecgOrderMainPage) {
        JeecgOrderMain jeecgOrderMain = new JeecgOrderMain();
        BeanUtils.copyProperties(jeecgOrderMainPage, jeecgOrderMain);
        jeecgOrderMainService.updateById(jeecgOrderMain);
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
        jeecgOrderMainService.delMain(id);
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
        this.jeecgOrderMainService.removeByIds(Arrays.asList(ids.split(",")));
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
        JeecgOrderMain jeecgOrderMain = jeecgOrderMainService.getById(id);
        return Result.ok(jeecgOrderMain);
    }


    /**
     * 通过id查询
     *
     * @param jeecgOrderCustomer
     * @return
     */
    @GetMapping(value = "/listOrderCustomerByMainId")
    public Result<?> queryOrderCustomerListByMainId(JeecgOrderCustomer jeecgOrderCustomer,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<JeecgOrderCustomer> queryWrapper = QueryGenerator.initQueryWrapper(jeecgOrderCustomer, req.getParameterMap());
        Page<JeecgOrderCustomer> page = new Page<JeecgOrderCustomer>(pageNo, pageSize);
        IPage<JeecgOrderCustomer> pageList = jeecgOrderCustomerService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 通过id查询
     *
     * @param jeecgOrderTicket
     * @return
     */
    @GetMapping(value = "/listOrderTicketByMainId")
    public Result<?> queryOrderTicketListByMainId(JeecgOrderTicket jeecgOrderTicket,
                                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                  HttpServletRequest req) {
        QueryWrapper<JeecgOrderTicket> queryWrapper = QueryGenerator.initQueryWrapper(jeecgOrderTicket, req.getParameterMap());
        Page<JeecgOrderTicket> page = new Page<JeecgOrderTicket>(pageNo, pageSize);
        IPage<JeecgOrderTicket> pageList = jeecgOrderTicketService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param jeecgOrderCustomer
     * @return
     */
    @PostMapping(value = "/addCustomer")
    public Result<?> addCustomer(@RequestBody JeecgOrderCustomer jeecgOrderCustomer) {
        jeecgOrderCustomerService.save(jeecgOrderCustomer);
        return Result.ok("添加成功!");
    }

    /**
     * 编辑
     *
     * @param jeecgOrderCustomer
     * @return
     */
    @PutMapping("/editCustomer")
    public Result<?> editCustomer(@RequestBody JeecgOrderCustomer jeecgOrderCustomer) {
        jeecgOrderCustomerService.updateById(jeecgOrderCustomer);
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
        jeecgOrderCustomerService.removeById(id);
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
        this.jeecgOrderCustomerService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 添加
     *
     * @param jeecgOrderTicket
     * @return
     */
    @PostMapping(value = "/addTicket")
    public Result<?> addTicket(@RequestBody JeecgOrderTicket jeecgOrderTicket) {
        jeecgOrderTicketService.save(jeecgOrderTicket);
        return Result.ok("添加成功!");
    }

    /**
     * 编辑
     *
     * @param jeecgOrderTicket
     * @return
     */
    @PutMapping("/editTicket")
    public Result<?> editTicket(@RequestBody JeecgOrderTicket jeecgOrderTicket) {
        jeecgOrderTicketService.updateById(jeecgOrderTicket);
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
        jeecgOrderTicketService.removeById(id);
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
        this.jeecgOrderTicketService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

}