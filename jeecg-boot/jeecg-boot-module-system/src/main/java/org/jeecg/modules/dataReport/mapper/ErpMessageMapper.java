package org.jeecg.modules.dataReport.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.dataReport.entity.ErpMessage;

/**
 * @Description: erp审批消息查询
 * @Author: Zhao
 * @Date:   2019-10-22
 * @Version: V1.0
 */
public interface ErpMessageMapper extends BaseMapper<ErpMessage> {

    List<ErpMessage> getErpApproveMessage();

}
