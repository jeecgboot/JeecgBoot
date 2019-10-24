package org.jeecg.modules.dataReport.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.dataReport.entity.BillOrderData;

/**
 * @Description: 业务接单提单数据
 * @Author: Zhao
 * @Date:   2019-10-12
 * @Version: V1.0
 */
public interface BillOrderDataMapper extends BaseMapper<BillOrderData> {

    IPage<BillOrderData> getBillOrderDateList(Page<BillOrderData> page, @Param(Constants.WRAPPER) Wrapper<BillOrderData> queryWrapper);

    List<BillOrderData> getBillOrderDateList(@Param(Constants.WRAPPER) Wrapper<BillOrderData> queryWrapper);

}
