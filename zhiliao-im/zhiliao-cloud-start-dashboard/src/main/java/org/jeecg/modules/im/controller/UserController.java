package org.jeecg.modules.im.controller;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.tools.ToolDateTime;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.User;
import org.jeecg.modules.im.entity.UserSetting;
import org.jeecg.modules.im.entity.query_helper.QUser;
import org.jeecg.modules.im.service.*;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/im/user")
public class UserController extends BaseBackController {
    @Resource
    private UserService userService;
    @Resource
    private FriendService friendService;
    @Resource
    private DeviceService deviceService;
    @Resource
    private ContactService contactService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private UserSettingService userSettingService;

    @RequestMapping("/pagination")
    public Result<Object> list(QUser q,String regStart,String regEnd) {
        if(!isEmpty(regStart))
            q.setRegStartTime(ToolDateTime.getDate(regStart, ToolDateTime.pattern_ymd).getTime());
        if(!isEmpty(regEnd)) {
            q.setRegEndTime(ToolDateTime.getDate(regEnd, ToolDateTime.pattern_ymd).getTime());
            if(regStart.equals(regEnd)){
                q.setRegEndTime(ToolDateTime.getDateByDatePlusDays(ToolDateTime.getDate(q.getRegStartTime()),1).getTime());
            }
        }
        if(!isEmpty(q.getUsername())) {
            StringBuilder s = new StringBuilder();
            for (String username : StringUtils.split(q.getUsername(), ",")) {
                s.append("'").append(username).append("',");
            }
            q.setUsername(s.substring(0,s.toString().length()-1));
        }
        if(!isEmpty(q.getIds())) {
            StringBuilder s = new StringBuilder();
            for (String id : StringUtils.split(q.getIds(), ",")) {
                if(isEmpty(id)){
                    continue;
                }
                s.append("'").append(id).append("',");
            }
            q.setIds(s.substring(0,s.toString().length()-1));
        }
        return success(userService.pagination(new MyPage<>(getPage(), getPageSize()), q));
    }

    /**
     * 用户详情
     */
    @RequestMapping("/detail")
    public Result<Object> detail(@RequestParam Integer id) {
        User user = userService.findByIdWithInfo(id);
        user.setUserSetting(userSettingService.findByUserId(id));
        return success(user);
    }
    /**
     * 用户设置
     */
    @RequestMapping("/setting")
    public Result<Object> setting(@RequestParam Integer userId) {
        return success(userSettingService.findByUserId(userId));
    }
    /**
     * 用户设置
     */
    @RequestMapping("/update_setting")
    public Result<Object> updateSetting(@RequestBody UserSetting userSetting) {
        return success(userSettingService.updateSetting(userSetting));
    }
    /**
     * 创建或更新
     */
    @RequestMapping("/createOrUpdate")
    public Result<Object> createOrUpdate(@RequestBody @Validated User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return fail(bindingResult.getAllErrors().get(0));
        }
        return userService.consoleCreateOrUpdate(user);
    }
    /**
     * 踢下线
     */
    @RequestMapping("/kickOut")
    public Result<Object> kickOut(@RequestParam Integer userId) {
        return userService.consoleKickOut(userId);
    }

    /**
     * 设为默认添加的好友
     * @param userId
     * @return
     */
    @RequestMapping("/defaultFriend")
    public Result<Object> defaultFriend(@RequestParam Integer userId) {
        return userService.setDefaultFriend(userId);
    }
    /**
     * 禁言
     */
    @RequestMapping("/mute")
    public Result<Object> mute(@RequestParam Integer id,@RequestParam Long tsMute) {
        return userService.consoleMute(id,tsMute);
    }
    /**
     * 锁定
     */
    @RequestMapping("/lock")
    public Result<Object> lock(@RequestParam Integer id,@RequestParam Long tsLocked) {
        return userService.consoleLock(id,tsLocked);
    }
    /**
     * 禁止连接
     */
    @RequestMapping("/noConnect")
    public Result<Object> noConnect(@RequestParam Integer id,@RequestParam Long tsNoConnect) {
        return userService.consoleNoConnect(id,tsNoConnect);
    }


}
