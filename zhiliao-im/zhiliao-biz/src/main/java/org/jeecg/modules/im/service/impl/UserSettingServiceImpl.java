package org.jeecg.modules.im.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.constant.MsgType;
import org.jeecg.modules.im.entity.User;
import org.jeecg.modules.im.entity.UserSetting;
import org.jeecg.modules.im.mapper.UserSettingMapper;
import org.jeecg.modules.im.service.UserService;
import org.jeecg.modules.im.service.UserSettingService;
import org.jeecg.modules.im.service.XMPPService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.jeecg.modules.im.base.xmpp.MessageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 用户设置 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-04-13
 */
@Service
public class UserSettingServiceImpl extends BaseServiceImpl<UserSettingMapper, UserSetting> implements UserSettingService {

    @Autowired
    private UserSettingMapper userSettingMapper;
    @Resource
    private UserService userService;
    @Resource
    private XMPPService xmppService;
    @Override
    public UserSetting findByUserId(Integer userId) {
        return userSettingMapper.findByUserId(userId);
    }

    @Override
    public Result<Object> updateSetting(UserSetting setting) {
        try{
            User user = userService.findById(setting.getUserId());
            setting.setId(findByUserId(setting.getUserId()).getId());
            if(updateById(setting)){
                //发送邀请入群消息给用户
                MessageBean messageBean = new MessageBean();
                messageBean.setUserId(user.getId());
                messageBean.setContent(JSONObject.toJSONString(setting));
                messageBean.setType(MsgType.updateSetting.getType());
                xmppService.sendMsgToSelf(messageBean);
                return success();
            }
        }catch (Exception e){
        }
        return fail();
    }
}
