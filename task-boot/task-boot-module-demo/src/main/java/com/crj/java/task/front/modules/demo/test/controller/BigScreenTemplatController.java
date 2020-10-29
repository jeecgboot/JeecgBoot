package com.crj.java.task.front.modules.demo.test.controller;

import lombok.extern.slf4j.Slf4j;
import com.crj.java.task.front.common.system.base.controller.TaskController;
import com.crj.java.task.front.modules.demo.test.entity.TaskDemo;
import com.crj.java.task.front.modules.demo.test.service.ITaskDemoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 大屏预览入口
 * @Author: Crj
 * @Date:2019-12-12
 * @Version:V1.0
 */
@Slf4j
@Controller
@RequestMapping("/test/bigScreen/templat")
public class BigScreenTemplatController extends TaskController<TaskDemo, ITaskDemoService> {

    /**
     * @param modelAndView
     * @return
     */
    @RequestMapping("/html")
    public ModelAndView ftl(ModelAndView modelAndView) {
        modelAndView.setViewName("demo3");
        List<String> userList = new ArrayList<String>();
        userList.add("admin");
        userList.add("user1");
        userList.add("user2");
        log.info("--------------test--------------");
        modelAndView.addObject("userList", userList);
        return modelAndView;
    }

    /**
     * 生产销售监控模版
     * @param modelAndView
     * @return
     */
    @RequestMapping("/index1")
    public ModelAndView index1(ModelAndView modelAndView) {
        modelAndView.setViewName("/bigscreen/template1/index");
        return modelAndView;
    }

    /**
     * 智慧物流监控模版
     * @param modelAndView
     * @return
     */
    @RequestMapping("/index2")
    public ModelAndView index2(ModelAndView modelAndView) {
        modelAndView.setViewName("/bigscreen/template2/index");
        return modelAndView;
    }

}
