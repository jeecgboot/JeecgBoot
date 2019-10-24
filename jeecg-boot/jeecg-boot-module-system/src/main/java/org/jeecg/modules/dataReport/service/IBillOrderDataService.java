package org.jeecg.modules.dataReport.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.dataReport.entity.BillOrderData;

import java.util.List;

/**
 * @Description: 业务接单提单数据
 * @Author: Zhao
 * @Date:   2019-10-12
 * @Version: V1.0
 */
public interface IBillOrderDataService extends IService<BillOrderData> {

    IPage<BillOrderData> getBillOrderDateList(Page<BillOrderData> page, @Param(Constants.WRAPPER) Wrapper<BillOrderData> queryWrapper);

    List<BillOrderData> getBillOrderDateList(@Param(Constants.WRAPPER) Wrapper<BillOrderData> queryWrapper);

}
