package org.jeecg.modules.im.service.base;

import org.apache.shiro.authc.AuthenticationException;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtilApp;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.im.entity.User;
import org.jeecg.modules.im.service.UserService;

import javax.annotation.Resource;

public class BaseApiCtrl extends BaseController {
    @Resource
    private UserService userService;
    /**
     * 根据token获取当前用户
     *
     * @return
     */
    protected User getCurrentUser() {
        String token = request.getHeader(CommonConstant.X_ACCESS_TOKEN);
        if (oConvertUtils.isEmpty(token)) {
            token = request.getParameter("token");
        }
        // 解密获得userId，用于和数据库进行对比
        Integer userId = JwtUtilApp.getUserIdByToken(token);
        if (userId == null) {
            throw new AuthenticationException("token非法无效!");
        }

        // 查询用户信息
        User user = userService.findById(userId);
        if (user == null) {
            throw new AuthenticationException("用户不存在!");
        }
        // 判断用户状态
        if (user.getTsLocked()>0) {
            throw new AuthenticationException("账号已被锁定,请联系管理员!");
        }
        return user;
    }

    /**
     * 根据token获取当前用户id
     *
     * @return
     */
    protected Integer getCurrentUserId() {
        return getCurrentUser()!=null?getCurrentUser().getId():null;
    }
}
