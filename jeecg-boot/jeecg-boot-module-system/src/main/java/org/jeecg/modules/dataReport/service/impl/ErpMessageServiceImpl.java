package org.jeecg.modules.dataReport.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.dataReport.entity.ErpMessage;
import org.jeecg.modules.dataReport.mapper.ErpMessageMapper;
import org.jeecg.modules.dataReport.service.IErpMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: erp审批消息查询
 * @Author: Zhao
 * @Date:   2019-10-22
 * @Version: V1.0
 */
@Service
public class ErpMessageServiceImpl extends ServiceImpl<ErpMessageMapper, ErpMessage> implements IErpMessageService {

    @Autowired
    ErpMessageMapper erpMessageMapper;

    @DS("erp")
    @Override
    public List<ErpMessage> getErpApproveMessage() {

        return erpMessageMapper.getErpApproveMessage();
    }
}
