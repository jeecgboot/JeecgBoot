package org.jeecg.modules.im.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.constant.MsgType;
import org.jeecg.modules.im.base.xmpp.MessageBean;
import org.jeecg.modules.im.entity.MucMember;
import org.jeecg.modules.im.entity.MucPermission;
import org.jeecg.modules.im.entity.query_helper.QMucPermission;
import org.jeecg.modules.im.mapper.MucPermissionMapper;
import org.jeecg.modules.im.service.MucMemberService;
import org.jeecg.modules.im.service.MucPermissionService;
import org.jeecg.modules.im.service.XMPPService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 群组管理员权限 服务实现类
 * </p>
 *
 * @author junko
 * @since 2023-03-27
 */
@Service
@Slf4j
public class MucPermissionServiceImpl extends BaseServiceImpl<MucPermissionMapper, MucPermission> implements MucPermissionService {

    @Autowired
    private MucPermissionMapper mucPermissionMapper;
    @Autowired
    private MucMemberService mucMemberService;
    @Resource
    private XMPPService xmppService;
    @Override
    public MucPermission findByManager(Integer managerId) {
        return mucPermissionMapper.findByManager(managerId);
    }

    @Override
    public MucPermission findDefaultOfMuc(Integer mucId) {
        MucPermission permission = mucPermissionMapper.findDefaultOfMuc(mucId);
        if(permission==null){
            permission = new MucPermission();
            permission.setMucId(mucId);
            permission.setAddManager(false);
            permission.setAddMember(true);
            permission.setDelMember(true);
            permission.setModifyInfo(true);
            permission.setMuteMember(true);
            permission.setIsValidationTip(true);
            permission.setMsgPin(true);
            permission.setRevokeManager(false);
            permission.setModifyNotice(true);
            permission.setIsAnonymous(false);
            save(permission);
        }
        return permission;
    }

    @Override
    public Integer deleteOne(Integer mucId, Integer managerId) {
        return mucPermissionMapper.deleteOne(mucId,managerId);
    }

    @Override
    public Result<Object> findByUserOfMuc(Integer userId, Integer mucId) {
        try {
            MucMember member = mucMemberService.findByMucIdOfUser(mucId, userId);
            if (member == null || member.getRole() != MucMember.Role.Manager.getCode()) {
                return fail("非该群成员或非管理员");
            }
            return success(findByManager(member.getId()));
        }catch (Exception e){
            return fail();
        }
    }

    @Override
    public Result<Object> updateByCondition(QMucPermission q) {
        MucMember member;
        MucMember master = mucMemberService.getMaster(q.getMucId());
        if(q.getManagerId()==0){
            //群主
            member = master;
        }else{
            //管理员
            member = mucMemberService.getById(q.getManagerId());
        }
        if(member==null||member.getRole()<MucMember.Role.Manager.getCode()){
            log.error("非管理员或群主");
            return fail();
        }
        if(q.getTitle()!=null){
            if(!equals(member.getTitle(),q.getTitle())){
                member.setTitle(q.getTitle());
                mucMemberService.updateById(member);
                //发送修改头衔的群消息
                MessageBean msg = new MessageBean();
                msg.setUserId(master.getUserId());
                msg.setToUserId(member.getUserId());
                msg.setType(MsgType.modifyTitle.getType());
                msg.setMucId(q.getMucId());
                msg.setContent(q.getTitle());
                xmppService.sendMucMsg(msg);
            }
        }
        if(q.getUpdate()!=null&&q.getUpdate()==1){
            MucPermission permission = new MucPermission();
            BeanUtils.copyProperties(q,permission);
            if(q.getManagerId()==0){
                permission.setId(findDefaultOfMuc(q.getMucId()).getId());
            }else{
                permission.setId(findByManager(q.getManagerId()).getId());
            }
            updateById(permission);
            //发送修改群管理权限的单聊消息
            MessageBean msg = new MessageBean();
            msg.setUserId(master.getUserId());
            msg.setType(MsgType.modifyPermission.getType());
            msg.setMucId(q.getMucId());
            msg.setContent(JSONObject.toJSONString(permission));
//            if(q.getManagerId()==0) {
//                xmppService.sendMsgToSelf(msg);
//            }else{
                msg.setToUserId(member.getUserId());
                xmppService.sendMucMsg(msg);
//            }
        }
        return success();
    }
}
