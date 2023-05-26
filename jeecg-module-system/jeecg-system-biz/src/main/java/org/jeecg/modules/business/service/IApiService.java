package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.domain.api.wia.Parcel;

import java.util.List;

/**
 * @Description: 包裹
 * @Author: jeecg-boot
 * @Date: 2022-02-18
 * @Version: V1.0
 */
public interface IApiService extends IService<Parcel> {


    /**
     * 根据物流单号查询包裹轨迹
     */
    List<Parcel> searchByTrackingNumbers(List<String> trackingNumbers);

}
