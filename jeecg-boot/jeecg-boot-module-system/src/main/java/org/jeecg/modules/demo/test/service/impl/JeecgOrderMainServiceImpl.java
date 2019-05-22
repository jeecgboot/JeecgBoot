package org.jeecg.modules.demo.test.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.jeecg.modules.demo.test.entity.JeecgOrderCustomer;
import org.jeecg.modules.demo.test.entity.JeecgOrderMain;
import org.jeecg.modules.demo.test.entity.JeecgOrderTicket;
import org.jeecg.modules.demo.test.mapper.JeecgOrderCustomerMapper;
import org.jeecg.modules.demo.test.mapper.JeecgOrderMainMapper;
import org.jeecg.modules.demo.test.mapper.JeecgOrderTicketMapper;
import org.jeecg.modules.demo.test.service.IJeecgOrderMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 订单
 * @Author: jeecg-boot
 * @Date:  2019-02-15
 * @Version: V1.0
 */
@Service
public class JeecgOrderMainServiceImpl extends ServiceImpl<JeecgOrderMainMapper, JeecgOrderMain> implements IJeecgOrderMainService {

    @Autowired
    private JeecgOrderMainMapper jeecgOrderMainMapper;
    @Autowired
    private JeecgOrderCustomerMapper jeecgOrderCustomerMapper;
    @Autowired
    private JeecgOrderTicketMapper jeecgOrderTicketMapper;

    @Override
    @Transactional
    public void saveMain(JeecgOrderMain jeecgOrderMain, List<JeecgOrderCustomer> jeecgOrderCustomerList, List<JeecgOrderTicket> jeecgOrderTicketList) {
        jeecgOrderMainMapper.insert(jeecgOrderMain);
        if (jeecgOrderCustomerList != null) {
            for (JeecgOrderCustomer entity : jeecgOrderCustomerList) {
                entity.setOrderId(jeecgOrderMain.getId());
                jeecgOrderCustomerMapper.insert(entity);
            }
        }
        if (jeecgOrderTicketList != null) {
            for (JeecgOrderTicket entity : jeecgOrderTicketList) {
                entity.setOrderId(jeecgOrderMain.getId());
                jeecgOrderTicketMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void updateMain(JeecgOrderMain jeecgOrderMain, List<JeecgOrderCustomer> jeecgOrderCustomerList, List<JeecgOrderTicket> jeecgOrderTicketList) {
        jeecgOrderMainMapper.updateById(jeecgOrderMain);

        //1.先删除子表数据
        jeecgOrderTicketMapper.deleteTicketsByMainId(jeecgOrderMain.getId());
        jeecgOrderCustomerMapper.deleteCustomersByMainId(jeecgOrderMain.getId());

        //2.子表数据重新插入
        if (jeecgOrderCustomerList != null) {
            for (JeecgOrderCustomer entity : jeecgOrderCustomerList) {
                entity.setOrderId(jeecgOrderMain.getId());
                jeecgOrderCustomerMapper.insert(entity);
            }
        }
        if (jeecgOrderTicketList != null) {
            for (JeecgOrderTicket entity : jeecgOrderTicketList) {
                entity.setOrderId(jeecgOrderMain.getId());
                jeecgOrderTicketMapper.insert(entity);
            }
        }
    }

	@Override
	@Transactional
	public void delMain(String id) {
		jeecgOrderMainMapper.deleteById(id);
		jeecgOrderTicketMapper.deleteTicketsByMainId(id);
		jeecgOrderCustomerMapper.deleteCustomersByMainId(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			jeecgOrderMainMapper.deleteById(id);
			jeecgOrderTicketMapper.deleteTicketsByMainId(id.toString());
			jeecgOrderCustomerMapper.deleteCustomersByMainId(id.toString());
		}
	}

}
