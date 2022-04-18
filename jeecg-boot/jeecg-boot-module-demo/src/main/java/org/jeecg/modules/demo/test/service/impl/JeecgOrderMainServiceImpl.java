package org.jeecg.modules.demo.test.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(rollbackFor = Exception.class)
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

    /**
     * 一对多维护逻辑改造  LOWCOD-315
     * @param jeecgOrderMain
     * @param jeecgOrderCustomerList
     * @param jeecgOrderTicketList
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCopyMain(JeecgOrderMain jeecgOrderMain, List<JeecgOrderCustomer> jeecgOrderCustomerList, List<JeecgOrderTicket> jeecgOrderTicketList) {
        jeecgOrderMainMapper.updateById(jeecgOrderMain);

        // 循环前台传过来的数据
        for (JeecgOrderTicket ticket:jeecgOrderTicketList){
            // 先查询子表数据库
            JeecgOrderTicket orderTicket = jeecgOrderTicketMapper.selectById(ticket.getId());
            if(orderTicket == null){
                // 当传过来的id数据库不存在时，说明数据库没有，走新增逻辑
                ticket.setOrderId(jeecgOrderMain.getId());
                jeecgOrderTicketMapper.insert(ticket);
                break;
            }
            if(orderTicket.getId().equals(ticket.getId())){
                // 传过来的id和数据库id一至时，说明数据库存在该数据，走更新逻辑
                jeecgOrderTicketMapper.updateById(ticket);
            }
        }
        for (JeecgOrderCustomer customer:jeecgOrderCustomerList){
            // 先查询子表数据库
            JeecgOrderCustomer customers = jeecgOrderCustomerMapper.selectById(customer.getId());
            if(customers == null){
                // 当传过来的id数据库不存在时，说明数据库没有，走新增逻辑
                customer.setOrderId(jeecgOrderMain.getId());
                jeecgOrderCustomerMapper.insert(customer);
                break;
            }
            if(customers.getId().equals(customer.getId())){
                //TODO 传过来的id和数据库id一至时，说明数据库存在该数据，走更新逻辑
                jeecgOrderCustomerMapper.updateById(customer);
            }
        }
        // 当跟新和删除之后取差集， 当传过来的id不存在，而数据库存在时，说明已删除，走删除逻辑
        List<JeecgOrderTicket> jeecgOrderTickets = jeecgOrderTicketMapper.selectTicketsByMainId(jeecgOrderMain.getId());
        List<JeecgOrderTicket> collect = jeecgOrderTickets.stream()
                .filter(item -> !jeecgOrderTicketList.stream()
                .map(e -> e.getId())
                .collect(Collectors.toList())
                .contains(item.getId()))
                .collect(Collectors.toList());
        // for循环删除id
        for (JeecgOrderTicket ticket:collect){
            jeecgOrderTicketMapper.deleteById(ticket.getId());
        }

        List<JeecgOrderCustomer> jeecgOrderCustomers = jeecgOrderCustomerMapper.selectCustomersByMainId(jeecgOrderMain.getId());
        List<JeecgOrderCustomer> customersCollect = jeecgOrderCustomers.stream()
                .filter(item -> !jeecgOrderCustomerList.stream()
                        .map(e -> e.getId())
                        .collect(Collectors.toList())
                        .contains(item.getId()))
                .collect(Collectors.toList());
        //TODO for循环删除id
        for (JeecgOrderCustomer c:customersCollect){
            jeecgOrderCustomerMapper.deleteById(c.getId());
        }
    }
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		jeecgOrderMainMapper.deleteById(id);
		jeecgOrderTicketMapper.deleteTicketsByMainId(id);
		jeecgOrderCustomerMapper.deleteCustomersByMainId(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			jeecgOrderMainMapper.deleteById(id);
			jeecgOrderTicketMapper.deleteTicketsByMainId(id.toString());
			jeecgOrderCustomerMapper.deleteCustomersByMainId(id.toString());
		}
	}

}
