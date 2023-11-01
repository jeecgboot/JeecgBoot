package org.jeecg.modules.im.xxljob;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.im.entity.User;
import org.jeecg.modules.im.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 关注系统号
 */
@Component
@Slf4j
public class FollowSysJobHandler {

    @Resource
    private UserService userService;

    @XxlJob(value = "followSys")
    public ReturnT<String> followSys(String params) {
        List<User> sysUsers = userService.findSysUser();
        List<Integer> types = new ArrayList<>();
        types.add(User.Type.common.getCode());
        types.add(User.Type.business.getCode());
        //普通用户和业务号
        List<User> users = userService.findByTypes(types);
        for (User user : users) {
            userService.batchFollowSys(user.getId(),sysUsers);
        }
        return ReturnT.SUCCESS;
    }
}

