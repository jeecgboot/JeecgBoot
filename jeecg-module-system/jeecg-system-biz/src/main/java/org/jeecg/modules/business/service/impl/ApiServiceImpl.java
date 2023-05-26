package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.wia.Parcel;
import org.jeecg.modules.business.mapper.ApiMapper;
import org.jeecg.modules.business.service.IApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: API轨迹服务
 * @Author: jeecg-boot
 * @Date: 2022-11-08
 * @Version: V1.0
 */
@Service
@Slf4j
public class ApiServiceImpl extends ServiceImpl<ApiMapper, Parcel> implements IApiService {

    @Autowired
    private ApiMapper apiMapper;

    @Override
    public List<Parcel> searchByTrackingNumbers(List<String> trackingNumbers) {
        return apiMapper.searchByTrackingNumbers(trackingNumbers);
    }
}
