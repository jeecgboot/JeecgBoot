package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.domain.api.wia.Parcel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: API包裹轨迹
 * @Author: jeecg-boot
 * @Date: 2022-11-08
 * @Version: V1.0
 */
@Repository
public interface ApiMapper extends BaseMapper<Parcel> {

    List<Parcel> searchByTrackingNumbers(List<String> trackingNumbers);
}
