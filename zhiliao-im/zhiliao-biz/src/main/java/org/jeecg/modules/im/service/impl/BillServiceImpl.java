package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Bill;
import org.jeecg.modules.im.entity.query_helper.QBill;
import org.jeecg.modules.im.mapper.BillMapper;
import org.jeecg.modules.im.service.BillService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 账单 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-11-13
 */
@Service
public class BillServiceImpl extends BaseServiceImpl<BillMapper, Bill> implements BillService {
    @Autowired
    private BillMapper mapper;

    @Override
    public IPage<Bill> pagination(MyPage<Bill> page, QBill q) {
        return mapper.pagination(page,q);
    }

}
