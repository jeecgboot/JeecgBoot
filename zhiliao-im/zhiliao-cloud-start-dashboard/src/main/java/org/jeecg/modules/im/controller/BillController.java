package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.query_helper.QBill;
import org.jeecg.modules.im.service.BillService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 账单
 */
@RestController
@RequestMapping("/im/bill")
public class BillController extends BaseBackController {
    @Resource
    private BillService billService;

    @RequestMapping("/pagination")
    public Result<Object> list(QBill q){
        return success(billService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }
}
