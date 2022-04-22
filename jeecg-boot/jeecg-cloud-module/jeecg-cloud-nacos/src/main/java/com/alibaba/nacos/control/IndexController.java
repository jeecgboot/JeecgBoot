package com.alibaba.nacos.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 积木报表-设置默认首页跳转
 */
@Controller
public class IndexController {
    private Logger logger = LoggerFactory.getLogger(IndexController.class);

    @GetMapping("/")
    public String index(Model model) {
        return "/nacos"; // 视图重定向 - 跳转
    }
}