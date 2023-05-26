package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.entity.ParcelTrace;

import java.util.List;

/**
 * @Description: 包裹轨迹
 * @Author: jeecg-boot
 * @Date: 2022-02-18
 * @Version: V1.0
 */
public interface IParcelTraceService extends IService<ParcelTrace> {

    public List<ParcelTrace> selectByMainId(String mainId);
}
