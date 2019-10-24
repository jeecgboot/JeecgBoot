package org.jeecg.modules.dataReport.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.dataReport.entity.BillOrderData;
import org.jeecg.modules.dataReport.mapper.BillOrderDataMapper;
import org.jeecg.modules.dataReport.service.IBillOrderDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 业务接单提单数据
 * @Author: Zhao
 * @Date:   2019-10-12
 * @Version: V1.0
 */
@Service
public class BillOrderDataServiceImpl extends ServiceImpl<BillOrderDataMapper, BillOrderData> implements IBillOrderDataService {

    @Autowired
    BillOrderDataMapper billOrderDataMapper;

    @Override
    @DS("tms")
    public IPage<BillOrderData> getBillOrderDateList(Page<BillOrderData> page, Wrapper<BillOrderData> queryWrapper) {
        return billOrderDataMapper.getBillOrderDateList(page,queryWrapper);
    }

    @Override
    @DS("tms")
    public List<BillOrderData> getBillOrderDateList(Wrapper<BillOrderData> queryWrapper) {
        return billOrderDataMapper.getBillOrderDateList(queryWrapper);
    }
}
