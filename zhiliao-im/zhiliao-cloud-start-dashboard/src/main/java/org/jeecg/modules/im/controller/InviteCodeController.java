package org.jeecg.modules.im.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.InviteCode;
import org.jeecg.modules.im.entity.Link;
import org.jeecg.modules.im.entity.query_helper.QContact;
import org.jeecg.modules.im.entity.query_helper.QInviteCode;
import org.jeecg.modules.im.service.ContactService;
import org.jeecg.modules.im.service.InviteCodeService;
import org.jeecg.modules.im.service.MucService;
import org.jeecg.modules.im.service.UserService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/im/inviteCode")
public class InviteCodeController extends BaseBackController {
    @Resource
    private InviteCodeService inviteCodeService;
    @Resource
    private UserService userService;
    @Resource
    private MucService mucService;

    @RequestMapping("/pagination")
    public Result<Object> list(QInviteCode q){
        return success(inviteCodeService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }

    /**
     * 创建或更新
     */
    @RequestMapping("/createOrUpdate")
    public Result<Object> createOrUpdate(@RequestBody @Validated InviteCode code, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return fail(bindingResult.getAllErrors().get(0));
        }
        return inviteCodeService.createOrUpdate(code);
    }

    @RequestMapping("/detail")
    public Result<Object> detail(@RequestParam String id){
        return success(inviteCodeService.getById(id));
    }

    @RequestMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return inviteCodeService.del(ids);
    }

}
