package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.business.entity.ParcelTrace;
import org.jeecg.modules.business.mapper.ParcelTraceMapper;
import org.jeecg.modules.business.service.IParcelTraceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 包裹轨迹
 * @Author: jeecg-boot
 * @Date: 2022-02-18
 * @Version: V1.0
 */
@Service
public class ParcelTraceServiceImpl extends ServiceImpl<ParcelTraceMapper, ParcelTrace> implements IParcelTraceService {

    @Autowired
    private ParcelTraceMapper parcelTraceMapper;

    @Override
    public List<ParcelTrace> selectByMainId(String mainId) {
        return parcelTraceMapper.selectByMainId(mainId);
    }
}
