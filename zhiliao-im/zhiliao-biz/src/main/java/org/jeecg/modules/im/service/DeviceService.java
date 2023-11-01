package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Device;
import org.jeecg.modules.im.entity.query_helper.QDevice;

import java.util.List;

/**
 * <p>
 * 用户设备 服务类
 * </p>
 *
 * @author junko
 * @since 2021-02-06
 */
public interface  DeviceService extends IService<Device> {
    IPage<Device> pagination(MyPage<Device> page, QDevice q);

    List<Device> findAll(Integer userId);
    //可以离线推送的设备
    List<Device> findCanOfflinePush(Integer userId);
    //查找指定平台的推送id
    List<String> getJPushIds(Integer userId,String platform);

    Device findByPlatform(String deviceNo,String platform,Integer userId);
    Device findByDetail(String detail,Integer userId);
    //查询用户的设备数
    Integer getCountOfUser(Integer userId);

    Integer getOnlineCountOfUser(Integer userId);
    //当前在线数
    Integer getOnlineCount();
    //所有设备数
    Integer getTotal();
    //终止特定,except为true表终止除指定外所有
    Result<Object> terminate(Integer id,Boolean except);

    Result<Object> cleanJpushId(String jspushId,Integer id);
    //清空该用户所有的设备token
    Integer clearAllTokenOfUser(Integer userId);
}
