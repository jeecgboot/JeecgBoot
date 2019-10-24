package org.jeecg.modules.dataReport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.dataReport.entity.ErpMessage;

import java.util.List;

/**
 * @Description: erp审批消息查询
 * @Author: Zhao
 * @Date:   2019-10-22
 * @Version: V1.0
 */
public interface IErpMessageService extends IService<ErpMessage> {

    List<ErpMessage> getErpApproveMessage();

}
