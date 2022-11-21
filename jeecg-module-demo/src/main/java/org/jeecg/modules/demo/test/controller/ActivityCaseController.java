package org.jeecg.modules.demo.test.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.demo.test.entity.Activity;
import org.jeecg.modules.demo.test.entity.ActivityCase;
import org.jeecg.modules.demo.test.entity.ActivityUser;
import org.jeecg.modules.demo.test.service.IActivityCaseService;
import org.jeecg.modules.demo.test.service.IActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 单表示例
 * @Author: jeecg-boot
 * @Date:2018-12-29
 * @Version:V2.0
 */
@Slf4j
@Api(tags = "活动控制器")
@RestController
@RequestMapping("/api/biz/activity/case")
public class ActivityCaseController extends JeecgController<ActivityCase, IActivityCaseService> {

    @AutoLog(value = "生成活动url")
    @DeleteMapping(value = "/clean/{activityId}")
    @ApiOperation(value = "生成url", notes = "生成url")
    public Result<?> generateUrl(@PathVariable("activityId") String activityId) {
        log.info(" =========================================================== ");
        QueryWrapper<ActivityCase> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activity_id", activityId);
        getService().getBaseMapper().delete(queryWrapper);
        return Result.OK("活动数据清空成功！");
    }

}
