package org.jeecg.modules.im.controller;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.util.Kv;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Muc;
import org.jeecg.modules.im.entity.MucMember;
import org.jeecg.modules.im.entity.query_helper.QMucMember;
import org.jeecg.modules.im.service.MucMemberService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 群组成员
 */
@RestController
@RequestMapping("/a/mucMember")
public class MucMemberCtrl extends BaseApiCtrl {
    @Resource
    private MucMemberService mucMemberService;

    /**
     * 群的成员分页
     */
    @RequestMapping("/pagination")
    public Result<Object> page(QMucMember q){
        q.setStatus(MucMember.Status.Normal.getCode());
        if(StringUtils.isNotBlank(q.getMucIds())){
            Kv data = Kv.create();
            for (String mucId : StringUtils.split(q.getMucIds(),";")) {
                if(StringUtils.isBlank(mucId)){
                    continue;
                }
                q.setMucId(Integer.parseInt(mucId));
                data.put(mucId, mucMemberService.pageApi(new MyPage<>(getPage(),getPageSize()),q));
            }
            return success(data);
        }
        return success(mucMemberService.pageApi(new MyPage<>(getPage(),getPageSize()),q));
    }

    /**
     * 我的
     * @return
     */
    @RequestMapping("/mine")
    public Result<Object> mine(){
        return success(mucMemberService.findMine(getCurrentUserId()));
    }

    @RequestMapping("/getOne")
    public Result<Object> getOne(Integer id){
        return success(mucMemberService.findById(id));
    }

    /**
     * 移除群聊
     */
    @RequestMapping("/kick")
    public Result<Object> kick(@RequestParam Integer mucId,@RequestParam String memberIds){
        return mucMemberService.kick(getCurrentUserId(),mucId,memberIds);
    }
    //用户主动退群
    @RequestMapping("/quit")
    public Result<Object> quit(@RequestParam Integer mucId){
        return mucMemberService.quit(getCurrentUserId(),mucId);
    }


    /**
     * 查询用户在某个群的成员信息
     * @param mucId
     * @param userId
     * @return
     */
    @RequestMapping("/getByUserIdOfMuc")
    public Result<Object> getByUserIdOfMuc(@RequestParam Integer mucId,@RequestParam Integer userId){
        return success(mucMemberService.findByMucIdOfUser(mucId,userId));
    }

    /**
     * 查询所有
     * @param mucId
     * @return
     */
    @RequestMapping("/getAll")
    public Result<Object> getAll(@RequestParam Integer mucId){
        return success(mucMemberService.findAll(mucId));
    }
    /**
     * 更新群聊设置
     * 昵称、备注、聊天背景
     */
    @PostMapping("/update")
    public Result<Object> updateMember(MucMember member){
        return success(mucMemberService.updateMember(member));
    }
}
