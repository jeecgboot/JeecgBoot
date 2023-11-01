package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Customer;
import org.jeecg.modules.im.entity.Customer;
import org.jeecg.modules.im.entity.query_helper.QCustomer;
import org.jeecg.modules.im.mapper.CustomerMapper;
import org.jeecg.modules.im.mapper.CustomerMapper;
import org.jeecg.modules.im.service.CustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户 服务实现类
 * </p>
 *
 * @author junko
 * @since 2023-02-07
 */
@Service
public class CustomerServiceImpl extends BaseServiceImpl<CustomerMapper, Customer> implements CustomerService {
    @Autowired
    private CustomerMapper customerMapper;
    @Override
    public IPage<Customer> pagination(MyPage<Customer> page, QCustomer q) {
        return customerMapper.pagination(page,q);
    }

    @Override
    public Customer findByServerId(String serverId) {
        return customerMapper.findByServerId(serverId);
    }
}
