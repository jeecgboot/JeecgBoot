package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.im.entity.MucMember;
import org.jeecg.modules.im.entity.UserInfo;
import org.jeecg.modules.im.mapper.UserInfoMapper;
import org.jeecg.modules.im.service.*;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-09-21
 */
@Service
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;
    @Resource
    private MucService mucService;
    @Resource
    private FriendService friendService;
    @Resource
    private ContactService contactService;
    @Resource
    private DeviceService deviceService;
    @Override
    public UserInfo findBasicByUserId(Integer userId) {
        return userInfoMapper.findByUserId(userId);
    }
    @Override
    public UserInfo findByUserId(Integer userId) {
        UserInfo info = userInfoMapper.findByUserId(userId);
        info.setMucCreate(mucService.getCountOfRole(userId, MucMember.Role.Master));
        info.setMucManage(mucService.getCountOfRole(userId, MucMember.Role.Manager));
        info.setMucJoin(mucService.getCountOfRole(userId, MucMember.Role.Member));
        info.setFriendCount(friendService.getCountOfUser(userId));
        info.setDeviceCount(deviceService.getCountOfUser(userId));
        info.setContactCount(contactService.getCountOfUser(userId));
        return info;
    }
}
