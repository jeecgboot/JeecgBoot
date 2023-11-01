package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Customer;
import org.jeecg.modules.im.entity.Customer;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.query_helper.QCustomer;

/**
 * <p>
 * 客户 服务类
 * </p>
 *
 * @author junko
 * @since 2023-02-07
 */
public interface CustomerService extends IService<Customer> {
    IPage<Customer> pagination(MyPage<Customer> page, QCustomer q);
    Customer findByServerId(String serverId);

}
