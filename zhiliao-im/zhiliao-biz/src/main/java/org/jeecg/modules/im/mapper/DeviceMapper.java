package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Device;
import org.jeecg.modules.im.entity.query_helper.QDevice;

import java.util.List;

/**
 * <p>
 * 用户设备 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-02-06
 */
@Mapper
public interface DeviceMapper extends BaseMapper<Device> {
    MyPage<Device> pagination(@Param("pg") MyPage<Device> pg, @Param("q") QDevice q);

    List<Device> findAll(Integer userId);
    List<Device> findCanOfflinePush(Integer userId);
    List<String> getJPushIds(Integer userId,String platform);
    Device findByPlatform(String no,String platform, Integer userId);
    Device findByDetail(String detail, Integer userId);

    Integer getCountOfUser(Integer userId);
    Integer getOnlineCountOfUser(Integer userId);

    Integer getOnlineCount();

    Integer getTotal();
    Integer clearToken(Integer exceptId, Integer userId);

    int cleanJpushId(String jpushId, Integer id);

    Integer clearAllTokenOfUser(Integer userId);
}
