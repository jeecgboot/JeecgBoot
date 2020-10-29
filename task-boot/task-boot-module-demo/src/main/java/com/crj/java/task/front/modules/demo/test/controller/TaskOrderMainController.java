package com.crj.java.task.front.modules.demo.test.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crj.java.task.front.common.api.vo.Result;
import com.crj.java.task.front.common.system.base.controller.TaskController;
import com.crj.java.task.front.common.system.query.QueryGenerator;
import com.crj.java.task.front.common.system.vo.LoginUser;
import com.crj.java.task.front.modules.demo.test.entity.TaskOrderCustomer;
import com.crj.java.task.front.modules.demo.test.entity.TaskOrderMain;
import com.crj.java.task.front.modules.demo.test.entity.TaskOrderTicket;
import com.crj.java.task.front.modules.demo.test.service.ITaskOrderCustomerService;
import com.crj.java.task.front.modules.demo.test.service.ITaskOrderMainService;
import com.crj.java.task.front.modules.demo.test.service.ITaskOrderTicketService;
import com.crj.java.task.front.modules.demo.test.vo.TaskOrderMainPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description: 一对多示例（JEditableTable行编辑）
 * @Author: Crj
 * @Date:2019-02-15
 * @Version: V2.0
 */
@RestController
@RequestMapping("/test/taskOrderMain")
@Slf4j
public class TaskOrderMainController extends TaskController<TaskOrderMain, ITaskOrderMainService> {

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
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TaskOrderMain taskOrderMain, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
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
        taskOrderMainService.saveMain(taskOrderMain, taskOrderMainPage.getTaskOrderCustomerList(), taskOrderMainPage.getTaskOrderTicketList());
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param taskOrderMainPage
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> eidt(@RequestBody TaskOrderMainPage taskOrderMainPage) {
        TaskOrderMain taskOrderMain = new TaskOrderMain();
        BeanUtils.copyProperties(taskOrderMainPage, taskOrderMain);
        taskOrderMainService.updateMain(taskOrderMain, taskOrderMainPage.getTaskOrderCustomerList(), taskOrderMainPage.getTaskOrderTicketList());
        return Result.ok("编辑成功！");
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
        this.taskOrderMainService.delBatchMain(Arrays.asList(ids.split(",")));
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
     * @param id
     * @return
     */
    @GetMapping(value = "/queryOrderCustomerListByMainId")
    public Result<?> queryOrderCustomerListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<TaskOrderCustomer> taskOrderCustomerList = taskOrderCustomerService.selectCustomersByMainId(id);
        return Result.ok(taskOrderCustomerList);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryOrderTicketListByMainId")
    public Result<?> queryOrderTicketListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<TaskOrderTicket> taskOrderTicketList = taskOrderTicketService.selectTicketsByMainId(id);
        return Result.ok(taskOrderTicketList);
    }

    /**
     * 导出excel
     *
     * @param request
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TaskOrderMain taskOrderMain) {
        // Step.1 组装查询条件
        QueryWrapper<TaskOrderMain> queryWrapper = QueryGenerator.initQueryWrapper(taskOrderMain, request.getParameterMap());
        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        //获取当前用户
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        List<TaskOrderMainPage> pageList = new ArrayList<TaskOrderMainPage>();

        List<TaskOrderMain> taskOrderMainList = taskOrderMainService.list(queryWrapper);
        for (TaskOrderMain orderMain : taskOrderMainList) {
            TaskOrderMainPage vo = new TaskOrderMainPage();
            BeanUtils.copyProperties(orderMain, vo);
            // 查询机票
            List<TaskOrderTicket> taskOrderTicketList = taskOrderTicketService.selectTicketsByMainId(orderMain.getId());
            vo.setTaskOrderTicketList(taskOrderTicketList);
            // 查询客户
            List<TaskOrderCustomer> taskOrderCustomerList = taskOrderCustomerService.selectCustomersByMainId(orderMain.getId());
            vo.setTaskOrderCustomerList(taskOrderCustomerList);
            pageList.add(vo);
        }

        // 导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "一对多订单示例");
        // 注解对象Class
        mv.addObject(NormalExcelConstants.CLASS, TaskOrderMainPage.class);
        // 自定义表格参数
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("自定义导出Excel内容标题", "导出人:" + sysUser.getRealname(), "自定义Sheet名字"));
        // 导出数据列表
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(2);
            params.setNeedSave(true);
            try {
                List<TaskOrderMainPage> list = ExcelImportUtil.importExcel(file.getInputStream(), TaskOrderMainPage.class, params);
                for (TaskOrderMainPage page : list) {
                    TaskOrderMain po = new TaskOrderMain();
                    BeanUtils.copyProperties(page, po);
                    taskOrderMainService.saveMain(po, page.getTaskOrderCustomerList(), page.getTaskOrderTicketList());
                }
                return Result.ok("文件导入成功！");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败：" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.error("文件导入失败！");
    }

}
