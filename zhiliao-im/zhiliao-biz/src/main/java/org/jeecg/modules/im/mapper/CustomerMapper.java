package org.jeecg.modules.im.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Customer;
import org.jeecg.modules.im.entity.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.im.entity.query_helper.QCustomer;

/**
 * <p>
 * 客户 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2023-02-07
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
    MyPage<Customer> pagination(@Param("pg") MyPage<Customer> pg, @Param("q") QCustomer q);
    Customer findByServerId(String serverId);
}
