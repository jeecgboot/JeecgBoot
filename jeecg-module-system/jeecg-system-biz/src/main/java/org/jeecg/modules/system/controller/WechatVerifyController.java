package org.jeecg.modules.system.controller;

import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.PrintWriter;

/**
 * @Description: 企业微信证书验证
 * @author: wangshuai
 * @date: 2023/12/6 10:42
 */
@RestController
@Slf4j
public class WechatVerifyController {

    /**
     * 企业微信验证
     */
    @RequestMapping(value = "/WW_verify_{code}.txt")
    public void mpVerify(@PathVariable("code") String code, HttpServletResponse response) {
        try {
            PrintWriter writer = response.getWriter();
            writer.write(code);
            writer.close();
        } catch (Exception e) {
            log.error("企业微信证书验证失败！");
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }
}

