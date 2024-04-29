package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.entity.ParcelStatus;
import org.jeecg.modules.business.mapper.ParcelStatusMapper;
import org.jeecg.modules.business.service.IParcelStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: parce status
 * @Author: jeecg-boot
 * @Date:   2024-04-18
 * @Version: V1.0
 */
@Service
public class ParcelStatusServiceImpl extends ServiceImpl<ParcelStatusMapper, ParcelStatus> implements IParcelStatusService {
    @Autowired
    ParcelStatusMapper parcelStatusMapper;
}
