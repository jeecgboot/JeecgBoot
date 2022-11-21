package org.jeecg.modules.demo.test.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.demo.test.entity.ActivityUser;
import org.jeecg.modules.demo.test.service.IActivityUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 单表示例
 * @Author: jeecg-boot
 * @Date:2018-12-29
 * @Version:V2.0
 */
@Slf4j
@Api(tags = "活动控制器")
@RestController
@RequestMapping("/api/biz/activity/user")
public class ActivityUserController extends JeecgController<ActivityUser, IActivityUserService> {



}
