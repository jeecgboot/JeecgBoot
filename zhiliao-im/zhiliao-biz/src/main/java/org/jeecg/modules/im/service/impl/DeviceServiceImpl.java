package org.jeecg.modules.im.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.constant.MsgType;
import org.jeecg.modules.im.base.util.Kv;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Device;
import org.jeecg.modules.im.entity.query_helper.QDevice;
import org.jeecg.modules.im.mapper.DeviceMapper;
import org.jeecg.modules.im.service.DeviceService;
import org.jeecg.modules.im.service.LoginLogService;
import org.jeecg.modules.im.service.UserInfoService;
import org.jeecg.modules.im.service.XMPPService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.im.base.xmpp.MessageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户设备 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-02-06
 */
@Service
@Slf4j
public class DeviceServiceImpl extends BaseServiceImpl<DeviceMapper, Device> implements DeviceService {
    @Autowired
    private DeviceMapper deviceMapper;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private LoginLogService loginLogService;
    @Resource
    private XMPPService xmppService;

    @Override
    public IPage<Device> pagination(MyPage<Device> page, QDevice q) {
        return deviceMapper.pagination(page,q);
    }

    @Override
    public List<Device> findAll(Integer userId) {
        List<Device> devices = deviceMapper.findAll(userId);
        for (Device device : devices) {
            device.setLoginLog(loginLogService.findLatestByDeviceId(device.getId()));
        }
        return devices;
    }

    @Override
    public List<Device> findCanOfflinePush(Integer userId) {
        return deviceMapper.findCanOfflinePush(userId);
    }
    @Override
    public List<String> getJPushIds(Integer userId,String platform) {
        return deviceMapper.getJPushIds(userId,platform);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized Device findByPlatform(String no,String platform, Integer userId) {
        if(Device.Platform.getByName(platform)==null){
            log.error("platform "+platform+" is not found");
            return null;
        }
        Device device = deviceMapper.findByPlatform(no,platform, userId);
        if (device == null) {
            device = new Device();
            device.setNo(no);
            device.setPlatform(platform);
            device.setUserId(userId);
            device.setTsCreate(getTs());
            device.setTsDisabled(0L);
            save(device);
        }
        return device;
    }
    @Override
    public Device findByDetail(String detail, Integer userId) {
        return deviceMapper.findByDetail(detail, userId);
    }
    @Override
    public Integer getCountOfUser(Integer userId) {
        LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Device::getUserId,userId);
        return (int)count(wrapper);
    }
    @Override
    public Integer getOnlineCountOfUser(Integer userId) {
        return deviceMapper.getOnlineCountOfUser(userId);
    }

    @Override
    public Integer getOnlineCount() {
        return deviceMapper.getOnlineCount();
    }

    @Override
    public Integer getTotal() {
        return deviceMapper.getTotal();
    }

    @Override
    public Result<Object> terminate(Integer id,Boolean except) {
        Device device = getById(id);
        if(device==null||!device.getUserId().equals(getCurrentUserId())){
            return fail("会话不存在，无法操作！");
        }
        if(!except){
            device.setToken(null);
            updateById(device);
        }else{
            deviceMapper.clearToken(id,getCurrentUserId());
        }
        //无论是否在线，都发送终止命令
        MessageBean messageBean = new MessageBean();
        messageBean.setUserId(getCurrentUserId());
        Kv data = Kv.by("no",device.getNo()).set("except",except);
        messageBean.setContent(JSONObject.toJSONString(data));
        messageBean.setType(MsgType.terminate.getType());
        xmppService.sendMsgToSelf(messageBean);
        return success();
    }
    @Override
    public Result<Object> cleanJpushId(String jpushId,Integer id) {
        return success(deviceMapper.cleanJpushId(jpushId,id));
    }

    @Override
    public Integer clearAllTokenOfUser(Integer userId) {
        return deviceMapper.clearAllTokenOfUser(userId);
    }


}
