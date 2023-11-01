package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Bill;
import org.jeecg.modules.im.entity.Bill;
import org.jeecg.modules.im.entity.query_helper.QBill;

/**
 * <p>
 * 账单 服务类
 * </p>
 *
 * @author junko
 * @since 2021-03-25
 */
public interface BillService extends IService<Bill> {
    IPage<Bill> pagination(MyPage<Bill> page, QBill q);

}
